package glorydark.SimpleDialogues.dialogue;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.DyeColor;
import glorydark.SimpleDialogues.CreateFireworkApi;
import glorydark.SimpleDialogues.Dialogue;
import glorydark.SimpleDialogues.MainClass;
import glorydark.SimpleDialogues.Tools;
import glorydark.SimpleDialogues.charmsystem.CharmPoint;
import glorydark.SimpleDialogues.charmsystem.Window;
import glorydark.SimpleDialogues.particle.Particle;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DialoguePlayTask extends Task {
    private final Player player;
    private final Dialogue dialogue;
    private Integer tick = 0;
    @Override
    public void onRun(int i) {
        if(player == null || !player.isOnline()){
            MainClass.inDialogues.remove(player);
            this.cancel();
        }
        List<String> speakerContent = dialogue.getSpeakerContent();
        Integer interval = dialogue.getChangeInterval();
        String roleName = dialogue.getSpeakerName();
        // 64 bytes per line = 32 characters per line;
        // text left/right-align method : (64 - wholeCharLength) -> add " "
        int gapLength = 64 - Tools.getStringCharCount(roleName);
        String blank = "                                                                ";
        blank = blank.substring(0,gapLength-1);
        int index;
        if(tick != 0){
            index = tick/interval;
        }else{
            index = 0;
        }

        if(player != null && player.isOnline()) {
            if(index + 1 <= speakerContent.size()) {
                String finalRoleName = roleName;
                String content = speakerContent.get(index);
                if(content.split("\\|").length == 2){
                    finalRoleName = content.split("\\|")[0];
                    finalRoleName = finalRoleName.replace("@p", player.getName());
                    content = content.split("\\|")[1];
                }
                content = content.replace("@p", player.getName());
                if (player.level != null) {
                    content = content.replace("@level", player.getLevel().getName());
                }
                content = content.replace("@charmpoint", CharmPoint.getCharmPointCache(player).toString());
                Integer textLen = Tools.getStringCharCount(content); // 4  20
                int len = (int) ((double) (tick % interval) / interval * textLen);
                //showText = speakerContent.get(index).substring(0, len == 0? 1 : len-1);
                if (content.length() >= len && content.length() != textLen) {
                    if ((double) (tick % interval) / interval < 0.9 && interval >= 40) {
                        player.sendTip("[ " + finalRoleName + "§f ]" + blank + "\n  " + content.substring(0, len));
                    } else {
                        player.sendTip("[ " + finalRoleName + "§f ]" + blank + "\n  " + content);
                    }
                }
                if (len <= textLen && content.length() == textLen) {
                    if ((double) (tick % interval) / interval < 0.9 && interval >= 40) {
                        player.sendTip("[ " + finalRoleName + "§f ]" + blank + "\n  " + content.substring(0, len));
                    } else {
                        player.sendTip("[ " + finalRoleName + "§f ]" + blank + "\n  " + content);
                    }
                }
                Particle.CreateParticle(player, 0, player.getPosition(), 2);
            }
        }
        if(speakerContent.size()*interval < tick){
            //MainClass.plugin.getLogger().alert("dialogue test complete!");
            MainClass.inDialogues.remove(player);
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Integer i1 = random.nextInt(14);
            Integer i2 = random.nextInt(4);
            CreateFireworkApi.spawnFirework(player.getPosition(), CreateFireworkApi.getColorByInt(i1), CreateFireworkApi.getExplosionTypeByInt(i2));
            if(player != null) {
                if (dialogue.getFinishPlayers().contains(player.getName())) {
                    Window.showDialogueClearMenu(player, dialogue);
                    if (dialogue.canRegainRewards()) {
                        for (String cmd : dialogue.getExecuteCommands()) {
                            String command = cmd.replace("@p", player.getName());
                            Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), command);
                        }
                    }

                    if (dialogue.canRegainCharmPoint()) {
                        CharmPoint.addCharmPoint(player, dialogue.getCharmPoint());
                    }
                } else {
                    Window.showDialogueClearBonusMenu(player, dialogue);
                    for (String cmd : dialogue.getExecuteCommands()) {
                        String command = cmd.replace("@p", player.getName());
                        Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), command);
                    }
                    CharmPoint.addCharmPoint(player, dialogue.getCharmPoint());
                    dialogue.addFinishedPlayer(player);
                }
            }
            this.cancel();
        }
        tick++;
    }

    public DialoguePlayTask(Player player, Dialogue dialogue){
        this.player = player;
        this.dialogue = dialogue;
    }
}
