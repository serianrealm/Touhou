package com.touhou.strategy;

import java.util.List;

import com.touhou.components.Aircraft;
import com.touhou.components.Projectile;

public interface FireStrategy {
    List<Projectile> fire(Aircraft aircraft);
}
