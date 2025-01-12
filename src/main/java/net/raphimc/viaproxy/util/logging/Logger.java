/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2023 RK_01/RaphiMC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.raphimc.viaproxy.util.logging;

import com.mojang.authlib.GameProfile;
import com.viaversion.viaversion.api.connection.UserConnection;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.fusesource.jansi.AnsiConsole;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Locale;

public class Logger {

    public static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger("ViaProxy");

    public static final PrintStream SYSOUT = System.out;
    public static final PrintStream SYSERR = System.err;

    public static void setup() {
        if (System.console() != null) { // jANSI is the best lib. If there is no console it just segfaults the JVM process. Thanks!
            AnsiConsole.systemInstall();
        }
        System.setErr(new LoggerPrintStream("STDERR", SYSERR));
        System.setOut(new LoggerPrintStream("STDOUT", SYSOUT));
    }

    public static void u_info(final String title, final SocketAddress address, final GameProfile gameProfile, final String msg) {
        u_log(Level.INFO, title, address, gameProfile, msg);
    }

    public static void u_err(final String title, final SocketAddress address, final GameProfile gameProfile, final String msg) {
        u_log(Level.ERROR, title, address, gameProfile, msg);
    }

    public static void u_err(final String title, final UserConnection user, final String msg) {
        GameProfile gameProfile = null;
        if (user.getProtocolInfo().getUsername() != null) {
            gameProfile = new GameProfile(user.getProtocolInfo().getUuid(), user.getProtocolInfo().getUsername());
        }
        u_log(Level.ERROR, title, user.getChannel().remoteAddress(), gameProfile, msg);
    }

    public static void u_log(final Level level, final String title, final SocketAddress address, final GameProfile gameProfile, final String msg) {
        final InetSocketAddress socketAddress = (InetSocketAddress) address;
        LOGGER.log(level, "[" + title.toUpperCase(Locale.ROOT) + "] (" + socketAddress.getAddress().getHostAddress() + " | " + (gameProfile != null ? gameProfile.getName() : "null") + ") " + msg);
    }

}
