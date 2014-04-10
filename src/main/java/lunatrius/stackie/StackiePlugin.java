package lunatrius.stackie;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class StackiePlugin extends JavaPlugin {
	private Server server = null;
	private BukkitScheduler scheduler = null;
	private int taskId = -1;

	@Override
	public void onLoad() {
	}

	@Override
	public void onEnable() {
		this.saveDefaultConfig();

		Stackie stackie = Stackie.instance;
		stackie.interval = this.getConfig().getInt("interval", stackie.interval);
		stackie.distance = this.getConfig().getDouble("distance", stackie.distance);
		stackie.stackItems = this.getConfig().getBoolean("stack.items", stackie.stackItems);
		stackie.stackExperience = this.getConfig().getBoolean("stack.experience", stackie.stackExperience);

		this.server = getServer();
		this.scheduler = this.server.getScheduler();
		this.taskId = this.scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				Stackie.instance.stackEntities(StackiePlugin.this.server.getWorlds());
			}
		}, 0, 20);
	}

	@Override
	public void onDisable() {
		this.scheduler.cancelTask(this.taskId);
	}
}
