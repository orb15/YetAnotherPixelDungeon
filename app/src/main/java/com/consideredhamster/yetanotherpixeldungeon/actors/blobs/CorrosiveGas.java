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
package com.consideredhamster.yetanotherpixeldungeon.actors.blobs;

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ooze;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Elemental;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.BlobEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;

public class CorrosiveGas extends Blob {

    public CorrosiveGas() {
        super();

        name = "cloud of gas";
    }

	@Override
	protected void evolve() {
		super.evolve();
		
//		int levelDamage = 5 + Dungeon.depth * 5;
//
		Char ch;
		for (int i=0; i < LENGTH; i++) {
			if (cur[i] > 0 && (ch = Actor.findChar( i )) != null) {

//				int damage = (ch.HT + levelDamage) / 40;
//				if (Random.Int( 40 ) < (ch.HT + levelDamage) % 40) {
//					damage++;
//				}
//
//				ch.damage( damage, this );
//                if( ch.buff( Gas.class ) == null ) {
//                    Buff.prolong(ch, Gas.class, TICK).apply((int) (Math.sqrt(ch.HT) / 2), this );
//                } else {
//                    Buff.prolong(ch, Gas.class, TICK).proliferate( this );
//                }

                ch.damage( Random.IntRange( 1, (int)Math.sqrt( !Bestiary.isBoss(ch) ? ch.HT : ch.HT / 4 ) / 2 + 1 ), this, DamageType.ACID );

                Ooze buff = ch.buff( Ooze.class );

                if ( buff != null) {
                    buff.delay( TICK );
                }
//                Buff.affect(ch, Ooze.class);

                // FIXME

                if ( ch.buff( Burning.class ) != null || ch instanceof Elemental ) {
                    GameScene.add(Blob.seed(ch.pos, 2, Fire.class));
                }
			}
		}

        Blob blob = Dungeon.level.blobs.get( Fire.class );
        if (blob != null) {

            for (int pos=0; pos < LENGTH; pos++) {

                if ( cur[pos] > 0 && blob.cur[ pos ] < 2 ) {

                    int flammability = 0;

                    for (int n : Level.NEIGHBOURS8) {
                        if ( blob.cur[ pos + n ] > 0 ) {
                            flammability++;
                        }
                    }

                    if( Random.Int( 4 ) < flammability ) {

                        blob.volume += ( blob.cur[ pos ] = 2 );

                        volume -= ( cur[pos] / 2 );
                        cur[pos] -=( cur[pos] / 2 );

                    }

                }
            }
        }
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( Speck.factory( Speck.TOXIC ), 0.4f );
	}
	
	@Override
	public String tileDesc() {
		return "A greenish cloud of highly caustic gas is swirling here.";
	}
	
//	@Override
//	public void onDeath() {
//
//		Badges.validateDeathFromGas();
//
//		Dungeon.fail( Utils.format( ResultDescriptions.GAS, Dungeon.depth ) );
//		GLog.n( "You died from a toxic gas.." );
//	}
}
