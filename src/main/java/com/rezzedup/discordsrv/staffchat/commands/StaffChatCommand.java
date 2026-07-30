/*
 * The MIT License
 * Copyright © 2017-2024 RezzedUp and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rezzedup.discordsrv.staffchat.commands;

import com.rezzedup.discordsrv.staffchat.StaffChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import com.rezzedup.discordsrv.staffchat.StaffChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import java.util.Locale;
import static com.rezzedup.discordsrv.staffchat.util.Strings.colorful;

public class StaffChatCommand implements CommandExecutor {
	private final StaffChatPlugin plugin;

	public StaffChatCommand(StaffChatPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// 1. NEW TOGGLE LOGIC
		if (args.length >= 1 && args[0].equalsIgnoreCase("toggle")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can use this command.");
				return true;
			}

			Player player = (Player) sender;
			// Get the profile
			var profile = plugin.data().getOrCreateProfile(player);

			if (args.length >= 2) {
				String state = args[1].toLowerCase(Locale.ROOT);

				if (state.equals("yes") || state.equals("on")) {
					profile.automaticStaffChat(true); // Explicitly ENABLE
					player.sendMessage(colorful("&9StaffChat &fenabled."));
				} else if (state.equals("no") || state.equals("off")) {
					profile.automaticStaffChat(false); // Explicitly DISABLE
					player.sendMessage(colorful("&9StaffChat &fdisabled."));
				} else {
					player.sendMessage(colorful("&cUsage: /staffchat toggle <yes|no>"));
				}
			} else {
				player.sendMessage(colorful("&cUsage: /staffchat toggle <yes|no>"));
			}
			return true; // Stop here
		}

		// 2. EXISTING LOGIC
		if (args.length <= 0) {
			if (!(sender instanceof Player)) {
				return false;
			}
			plugin.data().getOrCreateProfile((Player) sender).toggleAutomaticStaffChat();
		} else {
			String message = String.join(" ", args);

			if (sender instanceof Player) {
				plugin.submitMessageFromPlayer((Player) sender, message);
			} else if (sender instanceof ConsoleCommandSender) {
				plugin.submitMessageFromConsole(message);
			} else {
				sender.sendMessage("Unsupported command sender type: " + sender.getClass().getSimpleName());
			}
		}

		return true;
	}
}
