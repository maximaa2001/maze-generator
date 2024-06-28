package com.maks.mazegenerator.service.animation;

import com.maks.mazegenerator.entity.AnimationHero;
import javafx.util.Pair;

import java.util.*;

public interface AnimationService {
    void runFrameAnimation(AnimationHero hero);
    void runTransitionAnimation(AnimationHero hero,  List<Pair<Integer, Integer>> path, double cellSideLength);
}
