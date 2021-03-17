package com.Jacrispys.ScavangerRace;

import com.Jacrispys.ScavangerRace.Listeners.UpdateListener;
import com.Jacrispys.ScavangerRace.commands.ScavRaceCMD;
import org.bukkit.plugin.java.JavaPlugin;

public class ScavangerRaceMain extends JavaPlugin {

    @Override
    public void onEnable() {
        new ScavRaceCMD(this);
        new UpdateListener(this);
    }
}
