package glorydark.SimpleDialogues;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import glorydark.SimpleDialogues.charmsystem.AchievementMain;
import glorydark.SimpleDialogues.charmsystem.CharmPoint;
import glorydark.SimpleDialogues.command.*;
import glorydark.SimpleDialogues.dialogue.DialogueMain;

import java.io.File;
import java.util.HashMap;

public class MainClass extends PluginBase implements Listener {
    public static MainClass plugin;
    public static String path;
    public static HashMap<String, Dialogue> dialoguesMap = new HashMap<>();
    public static HashMap<Player, Dialogue> inDialogues = new HashMap<>();

    @Override
    public void onLoad() {
        this.getLogger().info("SimpleDialog V1.0.0(beta1) 加载中");
        File file = new File(this.getDataFolder()+"/dialogues/");
        file.mkdir();
    }

    @Override
    public void onEnable() {
        this.getLogger().info("SimpleDialog V1.0.0(beta1) 已被启用");
        plugin = this;
        path = plugin.getDataFolder().getPath();
        DialogueMain.loadAll();
        CharmPoint.loadAll();
        AchievementMain.loadAll();
        this.getServer().getPluginManager().registerEvents(new EventListener(),this);
        this.getServer().getCommandMap().register("", new PlayDialogueCommand());
        this.getServer().getCommandMap().register("", new ShowRankingListCommand());
        this.getServer().getCommandMap().register("", new ShowAchievementCommand());
        this.getServer().getCommandMap().register("", new ReloadDialogueDataCommand());
        this.getServer().getCommandMap().register("", new AddCharmPointCommand());
        this.getServer().getCommandMap().register("", new ReduceCharmPointCommand());
        this.getServer().getCommandMap().register("", new SetCharmPointCommand());
        this.getServer().getCommandMap().register("", new HelpCommand());
    }

    @Override
    public void onDisable() {
        this.getLogger().info("SimpleDialog V1.0.0(beta1) 卸载中！");
        CharmPoint.saveAll();
        DialogueMain.saveAll();
        AchievementMain.saveAll();
    }
}
