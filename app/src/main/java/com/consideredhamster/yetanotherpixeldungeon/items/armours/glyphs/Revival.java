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

import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Bleeding;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Blindness;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Charm;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ooze;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Poison;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Stun;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Confusion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite.Glowing;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class Revival extends Armour.Glyph {

    private static final String TXT_RESURRECT	= "You are revived by the powers of your enchantment!";
	
	@Override
	public Glowing glowing() {
		return YELLOW;
	}

    @Override
    public Class<? extends DamageType> resistance() {
        return DamageType.Unholy.class;
    }

    @Override
    protected String name_p() {
        return "%s of revival";
    }

    @Override
    protected String name_n() {
        return "%s of martyrdom";
    }

    @Override
    protected String desc_p() {
        return "save you from death with a certain chance and make you more resistant to unholy damage";
    }

    @Override
    protected String desc_n() {
        return "prevent ankhs from working with a certain chance";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage ) {
        return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage ) {
        return false;
    }

    public static void resurrect( Hero hero ) {
        new Flare( 8, 32 ).color(0xFFFF66, true).show(hero.sprite, 2f) ;
        GameScene.flash(0xFFFFAA);

        hero.HP = hero.HT;
        Withered.detach(hero, Withered.class);
        Burning.detach(hero, Burning.class);
        Ooze.detach(hero, Ooze.class);
        Poison.detach(hero, Poison.class);
        Bleeding.detach(hero, Bleeding.class);
        Blindness.detach(hero, Blindness.class);
        Charm.detach(hero, Charm.class);
        Frozen.detach(hero, Frozen.class);
        Stun.detach(hero, Stun.class);
        Ensnared.detach(hero, Ensnared.class);
        Confusion.detach(hero, Confusion.class);

        hero.sprite.showStatus(CharSprite.POSITIVE, "resurrected!");
        GLog.w(TXT_RESURRECT);
    }
}
