package net.montoyo.mcef.setup;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CfgParser {

    private static abstract class Line {

        public abstract void write(BufferedWriter bw) throws IOException;
        public abstract void read(String content, Matcher m);

    }

    private static class CommentLine extends Line {

        private String data;

        public CommentLine(String d) {
            data = d;
        }

        @Override
        public void write(BufferedWriter bw) throws IOException {
            bw.write(data + "\n");
        }

        @Override
        public void read(String content, Matcher m) {
            data = content;
        }

    }

    private static class BeginCategoryLine extends Line {

        public static final String REGEX = "^(\\s*)([a-z]+)(\\s+)\\{(\\s*)$";
        private String prefix;
        private String category;
        private String inBetween;
        private String suffix;

        public BeginCategoryLine() {
        }

        public BeginCategoryLine(String name) {
            prefix = "";
            category = name;
            inBetween = " ";
            suffix = "";
        }

        @Override
        public void write(BufferedWriter bw) throws IOException {
            bw.write(prefix);
            bw.write(category);
            bw.write(inBetween);
            bw.write("{");
            bw.write(suffix + "\n");
        }

        @Override
        public void read(String content, Matcher m) {
            prefix = m.group(1);
            category = m.group(2);
            inBetween = m.group(3);
            suffix = m.group(4);
        }

        public String getCategoryName() {
            return category;
        }

    }

    private static class EndCategoryLine extends Line {

        public static final String REGEX = "^(\\s*)\\}(\\s*)$";
        private String prefix;
        private String suffix;

        public EndCategoryLine() {
            prefix = "";
            suffix = "";
        }

        @Override
        public void write(BufferedWriter bw) throws IOException {
            bw.write(prefix);
            bw.write("}");
            bw.write(suffix + "\n");
        }

        @Override
        public void read(String content, Matcher m) {
            prefix = m.group(1);
            suffix = m.group(2);
        }

    }

    private static class PropertyLine extends Line {

        public static final String REGEX = "^(\\s*)([A-Z])\\:([A-Za-z]+)=(.*)$";
        private String prefix;
        private char type;
        private String key;
        private String value;

        public PropertyLine() {
        }

        public PropertyLine(char t, String key, String val) {
            prefix = "    ";
            type = t;
            this.key = key;
            value = val;
        }

        @Override
        public void write(BufferedWriter bw) throws IOException {
            bw.write(prefix);
            bw.write(type + ":" + key);
            bw.write("=" + value + "\n");
        }

        @Override
        public void read(String content, Matcher m) {
            prefix = m.group(1);
            type = m.group(2).charAt(0);
            key = m.group(3);
            value = m.group(4);
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public boolean getBooleanValue(boolean def) {
            String data = value.trim().toLowerCase();

            if(data.equals("false"))
                return false;
            else if(data.equals("true"))
                return true;
            else {
                value = def ? "true" : "false";
                return def;
            }
        }

        public void setValue(String v) {
            value = v;
        }

    }

    private enum LineType {

        CATEGORY_BEGIN(BeginCategoryLine.REGEX, BeginCategoryLine.class),
        CATEGORY_END(EndCategoryLine.REGEX, EndCategoryLine.class),
        PROPERTY(PropertyLine.REGEX, PropertyLine.class);

        private final Pattern pattern;
        private final Class<? extends Line> cls;

        LineType(String regex, Class<? extends Line> cls) {
            pattern = Pattern.compile(regex);
            this.cls = cls;
        }

        public static Line parseLine(String l) {
            for(LineType lt : values()) {
                Matcher m = lt.pattern.matcher(l);

                if(m.matches()) {
                    try {
                        Line ret = lt.cls.newInstance();
                        ret.read(l, m);

                        return ret;
                    } catch(Throwable t) {
                        System.err.println("Could not instantiate line class \"" + lt.cls.getCanonicalName() + "\":");
                        t.printStackTrace();
                        return null;
                    }
                }
            }

            return null;
        }

    }

    private ArrayList<Line> lines = new ArrayList<Line>();
    private HashMap<String, HashMap<String, PropertyLine>> data = new HashMap<String, HashMap<String, PropertyLine>>();
    private File location;

    public CfgParser(File loc) {
        location = loc;
    }

    public boolean load() {
        try {
            unsafeRead();
            return true;
        } catch(Throwable t) {
            System.err.println("Could not read config file \"" + location.getAbsolutePath() + "\":");
            t.printStackTrace();
            return false;
        }
    }

    private void unsafeRead() throws Throwable {
        lines.clear();
        data.clear();

        BufferedReader br = new BufferedReader(new FileReader(location));
        String line;
        String currentCategory = null;
        int lineCnt = 0;

        while((line = br.readLine()) != null) {
            String trimmed = line.trim();
            Line l;

            if(trimmed.isEmpty() || trimmed.charAt(0) == '#')
                l = new CommentLine(line);
            else
                l = LineType.parseLine(line);

            if(l == null)
                throw new RuntimeException("Could not parse line #" + (lineCnt + 1) + ".");

            if(l instanceof BeginCategoryLine) {
                if(currentCategory == null) {
                    currentCategory = ((BeginCategoryLine) l).getCategoryName();
                    data.put(currentCategory, new HashMap<String, PropertyLine>());
                } else
                    throw new RuntimeException("At line #" + (lineCnt + 1) + ": Forgot to close brackets.");
            } else if(l instanceof EndCategoryLine) {
                if(currentCategory == null)
                    throw new RuntimeException("At line #" + (lineCnt + 1) + ": Closing non-opened bracket.");
                else
                    currentCategory = null;
            } else if(l instanceof PropertyLine) {
                if(currentCategory == null)
                    throw new RuntimeException("At line #" + (lineCnt + 1) + ": Setting property outside brackets.");
                else
                    data.get(currentCategory).put(((PropertyLine) l).getKey(), (PropertyLine) l);
            }

            lines.add(l);
            lineCnt++;
        }

        SetupUtil.silentClose(br);
    }

    public boolean save() {
        try {
            unsafeWrite();
            return true;
        } catch(Throwable t) {
            System.err.println("Could not write config file \"" + location.getAbsolutePath() + "\":");
            t.printStackTrace();
            return false;
        }
    }

    private void unsafeWrite() throws Throwable {
        BufferedWriter bw = new BufferedWriter(new FileWriter(location));
        for(Line l : lines)
            l.write(bw);

        SetupUtil.silentClose(bw);
    }

    private int findCategoryBeginning(String cat) {
        for(int i = 0; i < lines.size(); i++) {
            Line l = lines.get(i);

            if(l instanceof BeginCategoryLine && ((BeginCategoryLine) l).getCategoryName().equals(cat))
                return i;
        }

        return -1;
    }

    private PropertyLine getValue(char type, String category, String key, String def) {
        HashMap<String, PropertyLine> subdata;

        if(data.containsKey(category)) {
            subdata = data.get(category);

            if(subdata.containsKey(key))
                return subdata.get(key);
            else {
                int pos = findCategoryBeginning(category);
                if(pos < 0)
                    throw new RuntimeException("Could not find beginning for category \"" + category + "\"! This should NOT happen!");

                PropertyLine pl = new PropertyLine(type, key, def);
                lines.add(pos + 1, pl);
                subdata.put(key, pl);
                return pl;
            }
        } else {
            BeginCategoryLine bcl = new BeginCategoryLine(category);
            PropertyLine pl = new PropertyLine(type, key, def);
            EndCategoryLine ecl = new EndCategoryLine();

            lines.add(bcl);
            lines.add(pl);
            lines.add(ecl);

            subdata = new HashMap<String, PropertyLine>();
            subdata.put(key, pl);
            data.put(category, subdata);
            return pl;
        }
    }

    public String getStringValue(String category, String key, String def) {
        return getValue('S', category, key, def).getValue();
    }

    public boolean getBooleanValue(String category, String key, boolean def) {
        return getValue('B', category, key, def ? "true" : "false").getBooleanValue(def);
    }

    public void setStringValue(String category, String key, String val) {
        getValue('S', category, key, val).setValue(val);
    }

    public void setBooleanValue(String category, String key, boolean val) {
        String data = val ? "true" : "false";
        getValue('B', category, key, data).setValue(data);
    }

}
