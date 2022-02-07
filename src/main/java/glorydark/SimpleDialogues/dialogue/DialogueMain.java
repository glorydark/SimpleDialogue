package glorydark.SimpleDialogues.dialogue;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import glorydark.SimpleDialogues.Dialogue;
import glorydark.SimpleDialogues.MainClass;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class DialogueMain {
    public static void loadAll(){
        File path = new File(MainClass.path+"/dialogues/");
        if(path.exists()) {
            File[] files = path.listFiles();
            if (files != null && files.length > 0) {
                MainClass.dialoguesMap = new HashMap<>();
                HashMap<String, Dialogue> dialoguesMap = new HashMap<>();
                for (File file : files) {
                    Config config = new Config(file, Config.YAML);
                    Dialogue dialogue = Dialogue.parseConfig(config);
                    if (dialogue != null) {
                        String name = file.getName().split("\\.")[0];
                        dialoguesMap.put(name, dialogue);
                        MainClass.plugin.getLogger().info(TextFormat.GREEN + "Dialogue: " + name + " loaded");
                    } else {
                        MainClass.plugin.getLogger().alert("Dialogue File: " + file.getName() + " can not be loaded");
                    }
                }
                MainClass.dialoguesMap = dialoguesMap;
                MainClass.plugin.getLogger().info(TextFormat.GREEN + "All the dialogue files have been loaded!");
            } else {
                MainClass.plugin.getLogger().info(TextFormat.GREEN + "No dialogue file has been loaded!");
            }
        }
    }

    public static void saveAll(){
        for(String s: MainClass.dialoguesMap.keySet()){
            Config config = new Config(MainClass.path+"/dialogues/"+s+".yml",Config.YAML);
            List<String> stringList = config.getStringList("完成玩家");
            if(stringList != null){
                config.set("完成玩家", MainClass.dialoguesMap.get(s).getFinishPlayers());
                config.save();
            }
        }
        MainClass.plugin.getLogger().info(TextFormat.GREEN + "Dialogue Records have been saved!");
    }

    public static Dialogue getDialogue(String name){
        return MainClass.dialoguesMap.getOrDefault(name, null);
    }

    public static void showPlayerDialogue(Player player, String dialogueName) {
        Dialogue dialogue = MainClass.dialoguesMap.get(dialogueName);
        if (dialogue != null) {
            if(!MainClass.inDialogues.containsKey(player)) {
                MainClass.inDialogues.put(player, dialogue);
                MainClass.plugin.getServer().getScheduler().scheduleRepeatingTask(new DialoguePlayTask(player, dialogue), 1);
            }else{
                player.sendMessage(TextFormat.RED+"您正处于对话中，无法加入其他对话！");
            }
        }else{
            player.sendMessage(TextFormat.RED+"对话不存在！");
        }
    }
}
