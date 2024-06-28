package com.maks.mazegenerator.service.animation;

import com.maks.mazegenerator.entity.AnimationHero;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Pair;

import java.util.*;

public class AnimationServiceImpl implements AnimationService {

    @Override
    public void runFrameAnimation(AnimationHero hero) {
        Timeline timeline = new Timeline(new KeyFrame(hero.getFrameDuration(), event -> hero.nextFrame()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @Override
    public void runTransitionAnimation(AnimationHero hero, List<Pair<Integer, Integer>> path, double cellSideLength) {
        PathTransition transition = new PathTransition(TransitionDurationFactory.createTime(path.size()), new Path(resolvePathElements(hero, path, cellSideLength)), hero.getImageView());
        transition.play();
    }

    private List<PathElement> resolvePathElements(AnimationHero hero, List<Pair<Integer, Integer>> path, double cellSideLength) {
        List<PathElement> pathElements = new ArrayList<>();
        double currentX = hero.getImageView().getX() + cellSideLength / 2;
        double currentY = hero.getImageView().getY() + cellSideLength / 2;
        pathElements.add(new MoveTo(currentX, currentY));
        for (int i = 0; i < path.size() - 1; i++) {
            Pair<Integer, Integer> from = path.get(i);
            Pair<Integer, Integer> to = path.get(i + 1);
            int fromRow = from.getKey();
            int fromColumn = from.getValue();
            int toRow = to.getKey();
            int toColumn = to.getValue();
            if (fromRow == toRow) {
                if (toColumn > fromColumn) {
                    currentX += cellSideLength;
                } else {
                    currentX -= cellSideLength;
                }
            } else if (fromColumn == toColumn) {
                if (toRow > fromRow) {
                    currentY += cellSideLength;
                } else {
                    currentY -= cellSideLength;
                }
            }
            pathElements.add(new LineTo(currentX, currentY));
        }
        return pathElements;
    }
}
