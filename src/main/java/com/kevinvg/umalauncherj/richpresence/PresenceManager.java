package com.kevinvg.umalauncherj.richpresence;

import io.quarkus.runtime.Shutdown;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

@Singleton
@Slf4j
public class PresenceManager {
    private static final String APP_ID = "954453106765225995";

    DiscordRichPresence currentPresence;

    @Startup
    void setup() {
        DiscordRPC.discordInitialize(APP_ID, new DiscordEventHandlers.Builder().setReadyEventHandler(user -> log.info("Discord RPC ready")).build(), true);
        currentPresence = PresenceFactory.defaultActivity();
    }

    public void setPresence(DiscordRichPresence presence) {
        log.info("Queueing presence: {} {}", presence.details, presence.state);
        currentPresence = presence;
    }

    @Scheduled(every = "10s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void updateActivity() {
        if (currentPresence == null) {
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
