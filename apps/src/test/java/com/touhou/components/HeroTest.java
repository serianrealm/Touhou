package com.touhou.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeroTest {
    @BeforeEach
    void setUp() {
        Hero.resetInstance();
    }

    @AfterEach
    void tearDown() {
        Hero.resetInstance();
    }

    @Test
    void moveToUpdatesHeroPosition() {
        Hero hero = Hero.getInstance(100, 200);

        hero.moveTo(300, 450);

        assertEquals(300, hero.getX());
        assertEquals(450, hero.getY());
    }

    @Test
    void applyDamageFromParentClassReducesHealth() {
        Hero hero = Hero.getInstance(100, 200);

        hero.applyDamage(3);

        assertEquals(hero.getMaxHealth() - 3, hero.getHealth());
    }

    @Test
    void tickFromParentClassAdvancesFireCooldown() {
        Hero hero = Hero.getInstance(100, 200);

        assertEquals(1, hero.tryFire().size());
        assertTrue(hero.tryFire().isEmpty());

        for (int index = 0; index < 12; index++) {
            hero.tick();
        }

        assertEquals(1, hero.tryFire().size());
    }

    @Test
    void healthSupplyDoesNotExceedMaximumHealth() {
        Hero hero = Hero.getInstance(100, 200);
        hero.applyDamage(1);

        hero.applyHealthSupply();
        hero.applyHealthSupply();

        assertEquals(hero.getMaxHealth(), hero.getHealth());
    }

    @Test
    void temporaryWeaponRestoreReturnsToDefaultStrategy() {
        Hero hero = Hero.getInstance(100, 200);

        hero.applyFirepowerSupply(8);
        hero.restoreDefaultWeapon();

        assertEquals("SingleForwardFireStrategy", hero.getFireStrategy().getClass().getSimpleName());
    }
}
