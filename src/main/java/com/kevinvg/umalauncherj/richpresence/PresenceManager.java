package com.kevinvg.umalauncherj.richpresence;

import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import io.quarkus.runtime.Shutdown;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

@Singleton
@Slf4j
public class PresenceManager {
    private static final String APP_ID = "954453106765225995";
    private final AppSettingsManager settings;

    PresenceFactory presenceFactory;
    DiscordRichPresence currentPresence;

    @Inject
    public PresenceManager(AppSettingsManager settings, PresenceFactory presenceFactory) {
        this.settings = settings;
        this.presenceFactory = presenceFactory;
    }

    @Startup
    void setup() {
        DiscordRPC.discordInitialize(APP_ID, new DiscordEventHandlers.Builder().setReadyEventHandler(user -> log.info("Discord RPC ready")).build(), true);
        currentPresence = presenceFactory.defaultActivity();
    }

    public void setPresence(DiscordRichPresence presence) {
        log.info("Queueing presence: {} {}", presence.details, presence.state);
        currentPresence = presence;
    }

    @Scheduled(every = "10s", executionMaxDelay = "500ms", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void updateActivity() {
        if (currentPresence == null) {
            return;
        }

        if (Boolean.FALSE.equals(settings.<Boolean>get(AppSettings.SettingKey.ENABLE_RICH_PRESENCE))) {
            DiscordRPC.discordClearPresence();
            return;
        }


        log.info("Updating presence: presence: {} {}", currentPresence.details, currentPresence.state);
        DiscordRPC.discordUpdatePresence(currentPresence);
    }

    @Scheduled(every = "0.5s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void runCallbacks() {
        DiscordRPC.discordRunCallbacks();
    }

    @Shutdown
    void shutdown() {
        log.info("Shutting down RPC");
        DiscordRPC.discordShutdown();
    }
}
