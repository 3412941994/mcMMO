package com.gmail.nossr50.commands.experience;

import com.gmail.nossr50.datatypes.experience.XPGainReason;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.util.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MmoeditCommand extends ExperienceCommand {
    public MmoeditCommand(mcMMO pluginRef) {
        super(pluginRef);
    }

    @Override
    protected boolean permissionsCheckSelf(CommandSender sender) {
        return Permissions.mmoedit(sender);
    }

    @Override
    protected boolean permissionsCheckOthers(CommandSender sender) {
        return Permissions.mmoeditOthers(sender);
    }

    @Override
    protected void handleCommand(Player player, PlayerProfile profile, PrimarySkillType skill, int value) {
        int skillLevel = profile.getSkillLevel(skill);
        double xpRemoved = profile.getSkillXpLevelRaw(skill);

        profile.modifySkill(skill, value);

        if (player == null) {
            profile.scheduleAsyncSave();
            return;
        }

        if (value == skillLevel) {
            return;
        }

        pluginRef.getEventManager().tryLevelEditEvent(player, skill, value, xpRemoved, value > skillLevel, XPGainReason.COMMAND, skillLevel);
    }

    @Override
    protected void handlePlayerMessageAll(Player player, int value) {
        player.sendMessage(pluginRef.getLocaleManager().getString("Commands.mmoedit.AllSkills.1", value));
    }

    @Override
    protected void handlePlayerMessageSkill(Player player, int value, PrimarySkillType skill) {
        player.sendMessage(pluginRef.getLocaleManager().getString("Commands.mmoedit.Modified.1", skill.getName(), value));
    }
}
