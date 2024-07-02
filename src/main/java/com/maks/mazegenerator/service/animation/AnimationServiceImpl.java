package com.maks.mazegenerator.service.animation;

import com.maks.mazegenerator.entity.AnimationHero;
import com.maks.mazegenerator.util.VoidSmth;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AnimationServiceImpl implements AnimationService {
    private static final Logger logger = LoggerFactory.getLogger(AnimationServiceImpl.class);

    @Override
    public void runFrameAnimation(AnimationHero hero) {
        Timeline timeline = new Timeline(new KeyFrame(hero.getFrameDuration(), event -> hero.nextFrame()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        hero.setTimeline(timeline);
        timeline.play();
    }

    @Override
    public void runTransitionAnimation(AnimationHero hero, List<Pair<Integer, Integer>> path, double cellSideLength, VoidSmth onFinishedCallback) {
        logger.debug("path size {}", path.size());
        PathTransition transition = new PathTransition(TransitionDurationFactory.createTime(path.size()), new Path(resolvePathElements(hero, path, cellSideLength)), hero.getImageView());
        transition.setOnFinished((event -> onFinishedCallback.execute()));
        hero.setPathTransition(transition);
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
