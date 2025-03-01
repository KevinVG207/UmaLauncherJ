package com.kevinvg.umalauncherj.richpresence;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import io.quarkus.runtime.Shutdown;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Singleton
@Slf4j
public class PresenceManager {
    private static final long APP_ID = 954453106765225995L;

    private static Activity currentActivity;
    private static Activity newActivity;
    private final Core core;
    private final CreateParams params;
    private boolean stop = false;

    {
        File discordLibrary1;
        try {
            log.info("Downloading discord library");
            discordLibrary1 = downloadDiscordLibrary();
        } catch (IOException e) {
            log.error("Error downloading discord library", e);
            discordLibrary1 = null;
        }
        File discordLibrary = discordLibrary1;

        if (discordLibrary == null) {
            core = null;
            params = null;
        } else {
            Core.init(discordLibrary);

            params = new CreateParams();
            params.setClientID(APP_ID);
            params.setFlags(CreateParams.getDefaultFlags());

            core = new Core(params);

            setActivity(ActivityFactory.defaultActivity());
        }
    }

    public void setActivity(Activity activity) {
        log.info("Setting current activity to {}", activity);
        newActivity = activity;
    }

    @Startup
    void runCallbacks() {
        while (!stop){
            if (core != null) {
                core.runCallbacks();
            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(every = "5s")
    void updateActivity() {
        if (core == null) {
            return;
        }

        var tmp = currentActivity;
        currentActivity = newActivity;

        log.info("Updating core with activity {}", currentActivity);
        core.activityManager().updateActivity(currentActivity);

        if (tmp != null) {
            tmp.close();
        }
    }

    @Shutdown
    void shutdown() {
        log.info("Shutting down RPC");
        stop = true;
        if (params != null) params.close();
        if (core != null) core.close();
        if (currentActivity != null) currentActivity.close();
    }

    // Copied from https://github.com/JnCrMx/discord-game-sdk4j/blob/v0.5.5/examples/DownloadNativeLibrary.java
    private static File downloadDiscordLibrary() throws IOException
    {
        // Find out which name Discord's library has (.dll for Windows, .so for Linux)
        String name = "discord_game_sdk";
        String suffix;

        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

        if(osName.contains("windows"))
        {
            suffix = ".dll";
        }
        else if(osName.contains("linux"))
        {
            suffix = ".so";
        }
        else if(osName.contains("mac os"))
        {
            suffix = ".dylib";
        }
        else
        {
            throw new RuntimeException("cannot determine OS type: "+osName);
        }

		/*
		Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. Mac OS).
		At this point we need the "x86_64" version, as this one is used in the ZIP.
		 */
        if(arch.equals("amd64"))
            arch = "x86_64";

        // Path of Discord's library inside the ZIP
        String zipPath = "lib/"+arch+"/"+name+suffix;

        // Open the URL as a ZipInputStream
        URL downloadUrl = new URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip");
        ZipInputStream zin = new ZipInputStream(downloadUrl.openStream());

        // Search for the right file inside the ZIP
        ZipEntry entry;
        while((entry = zin.getNextEntry())!=null)
        {
            if(entry.getName().equals(zipPath))
            {
                // Create a new temporary directory
                // We need to do this, because we may not change the filename on Windows
                File tempDir = new File(System.getProperty("java.io.tmpdir"), "java-"+name+System.nanoTime());
                if(!tempDir.mkdir())
                    throw new IOException("Cannot create temporary directory");
                tempDir.deleteOnExit();

                // Create a temporary file inside our directory (with a "normal" name)
                File temp = new File(tempDir, name+suffix);
                temp.deleteOnExit();

                // Copy the file in the ZIP to our temporary file
                Files.copy(zin, temp.toPath());

                // We are done, so close the input stream
                zin.close();

                // Return our temporary file
                return temp;
            }
            // next entry
            zin.closeEntry();
        }
        zin.close();
        // We couldn't find the library inside the ZIP
        return null;
    }
}
