package io.github.spaceexplorers.screen;

import com.badlogic.gdx.Screen;
import io.github.spaceexplorers.SpaceExplorers;

public abstract class DefaultScreen implements Screen {

    protected final SpaceExplorers game;

    protected DefaultScreen(SpaceExplorers game) {
        this.game = game;
    }
}
