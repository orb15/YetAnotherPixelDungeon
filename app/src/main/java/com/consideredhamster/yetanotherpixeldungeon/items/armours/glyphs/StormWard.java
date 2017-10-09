/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2016 Considered Hamster
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs;

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Lightning;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;

import java.util.ArrayList;
import java.util.HashSet;

public class StormWard extends Armour.Glyph {

    private ArrayList<Char> affected = new ArrayList<Char>();

    private int[] points = new int[20];
    private int nPoints;

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }

    @Override
    public Class<? extends DamageType> resistance() {
        return DamageType.Shock.class;
    }

    @Override
    protected String name_p() {
        return "%s of storm ward";
    }

    @Override
    protected String name_n() {
        return "%s of potential";
    }

    @Override
    protected String desc_p() {
        return "shock your enemies on hit and give you lightning resistance";
    }

    @Override
    protected String desc_n() {
        return "shock you on hit";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        if (Level.adjacent(attacker.pos, defender.pos)) {
            points[0] = defender.pos;
            nPoints = 1;

            affected.clear();
            affected.add(defender);

            hit(attacker, Random.IntRange(damage / 4, damage / 3));

            attacker.sprite.parent.add(new Lightning(points, nPoints, null));

            return true;
        }

        return false;
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

        defender.damage(Random.IntRange(damage / 4, damage / 3), this, DamageType.SHOCK);

        return true;
    }

    private void hit( Char ch, int damage ) {

        if (damage < 1) {
            return;
        }

        affected.add(ch);

        ch.damage(damage, this, DamageType.SHOCK);

        points[nPoints++] = ch.pos;

        HashSet<Char> ns = new HashSet<Char>();
        for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
            Char n = Actor.findChar( ch.pos + Level.NEIGHBOURS8[i] );
            if (n != null && !affected.contains( n )) {
                ns.add( n );
            }
        }

        if (ns.size() > 0) {
            hit( Random.element( ns ), Random.IntRange( damage / 2, damage ) );
        }
    }
}
