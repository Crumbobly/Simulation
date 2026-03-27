package ru.lab.game;

import ru.lab.app.state.AppState;
import ru.lab.app.state.StateType;
import ru.lab.config.Config;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameController {

    private final AppState appState;
    private final GameContext context;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private boolean simulationStarted = false;

    private final List<Runnable> tickListeners = new ArrayList<>();

    public GameController(GameContext context, AppState appState) {
        this.appState = appState;
        this.context = context;

        appState.addChangeListener(change -> {
            if (change == StateType.SIM) {
                toggleSimulation();
            }
        });
    }

    public void addTickListener(Runnable listener) {
        tickListeners.add(listener);
    }

    public void toggleSimulation() {
        if (simulationStarted) {
            simulationStarted = false;
        } else {
            simulationStarted = true;
            startLoop();
        }
    }

    private void startLoop() {
        executor.submit(() -> {
            while (simulationStarted) {

                context.getSimulation().nextTurn();

                SwingUtilities.invokeLater(() -> {
                    for (Runnable listener : tickListeners) {
                        listener.run();
                    }
                });

                try {
                    Thread.sleep(Config.SIMULATION_DELAY_MS);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }


}
