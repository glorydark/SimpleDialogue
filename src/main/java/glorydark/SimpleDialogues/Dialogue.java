package glorydark.SimpleDialogues;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;

import java.util.ArrayList;
import java.util.List;

public class Dialogue {
    private String speakerName;
    private List<String> speakerContent;
    private Integer changeInterval;
    private List<String> executeCommands;
    private List<String> finishMessages;
    private Double charmPoint;
    private Boolean movable;
    private String dialogueName;
    private List<String> finishPlayers = new ArrayList<>();
    private Boolean canRegainRewards;
    private Boolean canRegainCharmPoint;

    public Dialogue(String dialogName, String speakerName, List<String> speakerContent, Integer changeInterval, List<String> executeCommands, List<String> finishMessages, Double charmPoint, Boolean movable){
        this.speakerName = speakerName;
        this.speakerContent = speakerContent;
        this.changeInterval = changeInterval;
        this.executeCommands = executeCommands;
        this.finishMessages = finishMessages;
        this.charmPoint = charmPoint;
        this.movable = movable;
    }

    public static Dialogue parseConfig(Config config){
        //check whether config is valid or not
        Dialogue dialogue = new Dialogue("dialogueName","speakerName", new ArrayList<>(),0, new ArrayList<>(), new ArrayList<>(),0d,false);
        if(config.exists("对话名称") && config.exists("对话人物") && config.exists("对话内容") && config.exists("内容切换间隔") && config.exists("完成执行指令") && config.exists("完成消息") && config.exists("魅力值") && config.exists("允许移动") && config.exists("完成玩家") && config.exists("重复执行指令") && config.exists("重复获取魅力值")){
            dialogue.speakerName = config.getString("对话人物");
            dialogue.movable = config.getBoolean("允许移动");
            dialogue.charmPoint = config.getDouble("魅力值");
            dialogue.finishMessages = config.getStringList("完成消息");
            dialogue.executeCommands = config.getStringList("完成执行指令");
            dialogue.changeInterval = config.getInt("内容切换间隔");
            dialogue.speakerContent = config.getStringList("对话内容");
            dialogue.dialogueName = config.getString("对话名称");
            dialogue.finishPlayers = config.getStringList("完成玩家");
            dialogue.canRegainCharmPoint = config.getBoolean("重复获取魅力值");
            dialogue.canRegainRewards = config.getBoolean("重复执行指令");
            return dialogue;
        }else{
            MainClass.plugin.getLogger().error("Can not parse the config");
            return null;
        }
    }

    public Boolean getMovable() {
        return movable;
    }

    public Double getCharmPoint() {
        return charmPoint;
    }

    public List<String> getExecuteCommands() {
        return executeCommands;
    }

    public List<String> getFinishMessages() {
        return finishMessages;
    }

    public Integer getChangeInterval() {
        return changeInterval;
    }

    public List<String> getSpeakerContent() {
        return speakerContent;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public String getDialogueName() {
        return dialogueName;
    }

    public List<String> getFinishPlayers() {
        return finishPlayers;
    }

    public Boolean isPlayerFinished(Player player){
        if(player == null){ return false;}
        return finishPlayers.contains(player.getName());
    }

    public Boolean canRegainRewards() {
        return canRegainRewards;
    }

    public Boolean canRegainCharmPoint() {
        return canRegainCharmPoint;
    }

    public void addFinishedPlayer(Player player){
        if(player != null && !finishPlayers.contains(player.getName())){
            finishPlayers.add(player.getName());
        }
    }
}
