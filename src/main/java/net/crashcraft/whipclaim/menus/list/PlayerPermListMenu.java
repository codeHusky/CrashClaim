package net.crashcraft.whipclaim.menus.list;

import dev.whip.crashutils.menusystem.GUI;
import dev.whip.crashutils.menusystem.defaultmenus.PlayerListMenu;
import net.crashcraft.whipclaim.claimobjects.BaseClaim;
import net.crashcraft.whipclaim.claimobjects.Claim;
import net.crashcraft.whipclaim.claimobjects.SubClaim;
import net.crashcraft.whipclaim.menus.permissions.SimplePermissionMenu;
import net.crashcraft.whipclaim.permissions.PermissionHelper;
import net.crashcraft.whipclaim.permissions.PermissionRoute;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerPermListMenu {
    public PlayerPermListMenu(BaseClaim claim, Player viewer, GUI previous){
        if (!PermissionHelper.getPermissionHelper().hasPermission(claim, viewer.getUniqueId(), PermissionRoute.MODIFY_PERMISSIONS)){
            viewer.sendMessage(ChatColor.RED + "You no longer have sufficient permissions to continue");
            //Try and close an inventory, want to close a loose end just in case
            viewer.closeInventory();
            return;
        }

        ArrayList<UUID> uuids = new ArrayList<>(claim.getPerms().getPlayerPermissions().keySet());

        for (Player player : Bukkit.getOnlinePlayers()){
            if (isVanished(player)){
                continue;
            }

            if (!uuids.contains(player.getUniqueId())) {
                uuids.add(player.getUniqueId());
            }
        }

        uuids.remove(viewer.getUniqueId());    //Cant modify perms of yourself

        if (claim instanceof SubClaim){ //Owners permissions are off limits.
            SubClaim temp = (SubClaim) claim;
            uuids.remove(temp.getParent().getOwner());
        } else if (claim instanceof Claim){
            Claim temp = (Claim) claim;
            uuids.remove(temp.getOwner());
        } else {
            throw new RuntimeException("Claim was not of known type.");
        }

        new PlayerListMenu(viewer, previous, uuids, (gui, uuid) -> {
            new SimplePermissionMenu(viewer, claim, uuid, gui).open();
            return "";
        }).open();
    }

    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}