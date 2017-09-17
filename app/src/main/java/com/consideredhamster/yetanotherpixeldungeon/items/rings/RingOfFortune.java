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
package com.consideredhamster.yetanotherpixeldungeon.items.rings;

public class RingOfFortune extends Ring {

	{
		name = "Ring of Fortune";
        shortName = "Fo";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Fortune();
	}
	
	@Override
	public String desc() {
        return isTypeKnown() ?
                ( bonus < 0 && isIdentified() ? "Normally, this ring " : "This ring " ) +
                "blesses its wearer with greater luck, increasing amount of loot dropped from enemies when equipped." +
                ( bonus < 0 && isIdentified() ? " However, because this ring is cursed, its effects are reversed." : "" ) :
            super.desc();
	}
	
	public class Fortune extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                    "You don't feel anything special on equipping this ring. Is that good?" :
                    "You don't feel anything special on equipping this ring. Is that bad?" ;
        }
	}
}
