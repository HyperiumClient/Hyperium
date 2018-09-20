package com.chattriggers.ctjs.loader;

//public class UriScheme {
//    private static final String PROTOCOL = "chattriggers://";
//    private static final int PORT = 21965;
//    private static final String QUOTE = "\"";
//
//    public static void installUriScheme() {
//        try {
//            regAdd(
//                    " /f /ve /d " +
//                            quote("URL:chattriggers Protocol")
//            );
//
//            regAdd(
//                    " /f /v " +
//                            quote("URL Protocol") +
//                            " /d " +
//                            quote("")
//            );
//
//            String cp = CTJS.configLocation.getAbsolutePath();
//
//
//
//            String sep = File.separator;
//            String javaProgram = System.getProperty("java.home") + sep + "bin" + sep + "javaw.exe";
//
//            String value = ("\"" + javaProgram + "\" -cp \"" + cp
//                    + "\" com.chattriggers.ctjs.loader.UriScheme " + "\"%1\"").replace("\"", "\\\"");
//
//            regAdd(
//                    "\\shell\\open\\command /f /ve /d " +
//                            "\"" + value + "\""
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void createSocketListener() {
//        new Thread(UriScheme::socketListener, "CTJSSocketListener").start();
//    }
//
//    private static String quote(String toQuote) {
//        return QUOTE + toQuote + QUOTE;
//    }
//
//    private static void regAdd(String args) throws IOException, InterruptedException {
//        Process process = Runtime.getRuntime().exec("REG ADD HKCU\\Software\\Classes\\chattriggers" + args);
//        if (process.waitFor() != 0) {
//            throw new IOException("Error editing registry!");
//        }
//    }
//
//    private static void socketListener() {
//        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            while (!Thread.interrupted()) {
//                try (Socket clientSocket = serverSocket.accept()) {
//                    InputStream inputStream = clientSocket.getInputStream();
//                    String module = new BufferedReader(new InputStreamReader(inputStream))
//                            .lines().collect(Collectors.joining("\n"));
//                    ModuleManager.INSTANCE.importModule(module);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
