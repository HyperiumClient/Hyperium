package cc.hyperium.mods.autogg;

public class GGThread implements Runnable
{
    @Override
    public void run() {
        try {
            Thread.sleep(AutoGG.getInstance().getDelay() * 1000);
            AutoGG.getInstance().getMinecraft().thePlayer.sendChatMessage("/achat gg");
            Thread.sleep(2000L);
            AutoGG.getInstance().setRunning(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
