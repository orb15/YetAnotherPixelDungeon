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
package com.consideredhamster.yetanotherpixeldungeon.actors;

import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.Difficulties;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Exposed;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Guard;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Amok;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Challenge;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.ForceField;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Bleeding;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Blindness;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Charm;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ooze;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Confusion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Cripple;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Light;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Sleep;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Speed;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Levitation;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.MindVision;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Stun;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Poison;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Slow;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Terror;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.UnholyArmor;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.PoisonParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Deflection;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Door;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public abstract class Char extends Actor {

//	protected static final String TXT_HIT		= "%s hit %s";
//	protected static final String TXT_KILL		= "You %s...";
	protected static final String TXT_DEFEAT	= "%s is defeated!";

	protected static final String TXT_DODGED	= "dodged";
	protected static final String TXT_MISSED	= "missed";

	protected static final String TXT_GUARD 	= "guard";
	protected static final String TXT_AMBUSH	= "sneak attack!";
	protected static final String TXT_COUNTER	= "counter attack!";
	protected static final String TXT_EXPOSED	= "exposed!";

//	private static final String TXT_YOU_MISSED	= "%s %s your attack";
//	private static final String TXT_SMB_MISSED	= "%s %s %s's attack";

    protected static final int VIEW_DISTANCE	= 8;

	public int pos = 0;
	
	public CharSprite sprite;
	
	public String name = "mob";
	
	public int HT;
	public int HP;
	
	protected float baseSpeed	= 1;

	public boolean stunned      = false;
	public boolean rooted		= false;
	public boolean flying		= false;
    public boolean moving		= false;



	public int invisible		= 0;

	private HashSet<Buff> buffs = new HashSet<Buff>();
	
	@Override
	protected boolean act() {
		Dungeon.level.updateFieldOfView( this );

        moving = false;

        Guard guard = buff( Guard.class );

        if( guard != null ) {
            Buff.detach( guard );
        }

		return false;
	}




    private static final String POS			= "pos";
	private static final String TAG_HP		= "HP";
	private static final String TAG_HT		= "HT";
	private static final String BUFFS		= "buffs";

	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );

		bundle.put( POS, pos );
		bundle.put( TAG_HP, HP );
		bundle.put( TAG_HT, HT );
		bundle.put( BUFFS, buffs );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );
		
		pos = bundle.getInt( POS );
		HP = bundle.getInt( TAG_HP );
		HT = bundle.getInt( TAG_HT );

		for (Bundlable b : bundle.getCollection( BUFFS )) {
			if (b != null) {
				((Buff)b).attachTo( this );
			}
		}
	}

    public int viewDistance() {
//        return 6;
        return buff( Blindness.class ) == null ? VIEW_DISTANCE : 1 ;
    };
	
	public boolean attack( Char enemy ){

        boolean visibleFight = Dungeon.visible[ pos ] || Dungeon.visible[ enemy.pos ];

        int damageRoll = damageRoll();

        Guard guarded = enemy.buff( Guard.class );

        if( guarded != null && !ignoresAC() && ( !isRanged() || enemy.blocksRanged() ) &&
            Random.Float() < enemy.guardChance() && guard( damageRoll, enemy.guardStrength() )
        ) {

            guarded.proc( enemy.blocksRanged() );

            attackProc( enemy, damageRoll, true );

            enemy.defenseProc( this, damageRoll, true );

            if( !isRanged() && Random.Float() < counterChance() ){

                Exposed exposed = Buff.affect( this, Exposed.class, TICK );

                if( exposed != null ) {
                    exposed.object = enemy.id();
                }

            }

            return true;

        } else if( hit( this, enemy, isRanged() && !ignoresDistancePenalty(), false ) ) {

            boolean blocked = !ignoresAC() && ( guarded != null || Random.Float() < enemy.guardChance() * 0.5f );

            damageRoll = enemy.defenseProc( this, damageRoll, false );

            if( !ignoresAC() ) {

                int dr = enemy.armorClass( blocked );

                damageRoll = absorb( damageRoll, dr, damageType() != null );

            }

            damageRoll = attackProc( enemy, damageRoll, false );

            enemy.damage( damageRoll, this, damageType());

            if( guarded != null ) guarded.reset( enemy.blocksRanged() );

            if (enemy == Dungeon.hero) {

                if (damageRoll >= enemy.HP) {
                    Camera.main.shake(GameMath.gate(1, damageRoll / (enemy.HT / 4), 5), 0.3f);
                    GameScene.flash(0x330000);
                }

                Dungeon.hero.interrupt( "You were awoken by an attack!" );
            }

            if (visibleFight) {
                Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
                enemy.sprite.bloodBurstA(sprite.center(), damageRoll );
            }

            return true;

        } else {

            if ( visibleFight ) {
                Sample.INSTANCE.play(Assets.SND_MISS);
            }

            enemy.missed();

            return false;

        }
	}
	
	public static boolean hit( Char attacker, Char defender, boolean ranged, boolean magic ) {

        if( defender.buff( Guard.class ) != null )
            return true;

        if( defender.isOpenedTo(attacker) )
            return true;

        if( defender.isCharmedBy(attacker) )
            return true;

        int attRoll = ( magic ? attacker.magicSkill() : attacker.accuracy() );

        if( Level.fieldOfView[ defender.pos ] )
            attRoll *= 2;

        if( ranged ) {

            int distance = Math.min( 9, Level.distance(attacker.pos, defender.pos) );

            if( distance > 1 ) {
                attRoll = attRoll * (9 - distance);
            }
        }

        int defRoll = defender.dexterity();
        int impassable = 16;

        for (int n : Level.NEIGHBOURS8) {
            if( Actor.findChar( defender.pos + n ) != null || Level.solid[defender.pos + n] || Level.chasm[defender.pos + n] && !defender.flying ) {
                impassable--;
            }
        }

        defRoll = defRoll * impassable / 16;

		return attRoll > Random.Int( attRoll + defRoll );
	}

    public static int absorb( int damage, int armorClass ) {
        return absorb( damage, armorClass, false );
    }

    public static int absorb( int damage, int armorClass, boolean penetrate ) {
        return armorClass > 0 && damage > 0 ? damage * damage / ( damage + Random.Int(
                ( penetrate ? armorClass : armorClass * 2 ) + 1 ) ) : damage;
    }

//    public static boolean guard( int damage, int guardStrength, boolean penetrate ) {
//        return guardStrength > 0 && damage > 0 ? damage *  ) : damage;
//    }

    public static boolean guard( int damage, int guard ) {
        return damage < Random.Int( guard * 3 + 1 );
    }



    public void missed() {

        if ( sprite.visible ) {
            sprite.showStatus( CharSprite.NEUTRAL, dexterity() > 0 ? TXT_DODGED : TXT_MISSED );
        }

        if ( this == Dungeon.hero ) {
            Dungeon.hero.interrupt();
        }
    }
	
	public int accuracy() {
        return 0;
    }
	
	public int dexterity() {
		return 0;
	}

	public int magicSkill() {
		return 0;
	}

    public float attackDelay() {
        return TICK / attackSpeed();
    }

    public float attackSpeed() {
        return 1.0f;
    }


    public int damageRoll() {
        return 0;
    }

    public int armourAC() {
        return 0;
    }

    public int shieldAC() {
        return 0;
    }

    public int guardStrength() {
        return 0;
    }

    public int armorClass() {

        return armorClass( false );

    }

	public int armorClass( boolean withShield ) {

        if (buff( Frozen.class ) != null) {
            return 0;
        }

        float armourMod = 1.0f;

        if ( buff(Withered.class) != null ) {
            armourMod *= buff( Withered.class ).modifier();
        }

        if ( buff(Ooze.class) != null ) {
            armourMod *= 0.5f;
        }

		return Math.round( armourAC() + ( withShield ? shieldAC() : 0 ) * armourMod );

	}

    public float guardChance() {

        if ( buff( Frozen.class ) != null || buff( Stun.class ) != null )
            return 0.0f;

        float guardChance = 1.0f;

        if ( buff( Confusion.class ) != null ) {
            guardChance *= 0.5f;
        }

        if ( buff( Blindness.class ) != null ) {
            guardChance *= 0.5f;
        }

        return guardChance;
    }

    public float counterChance() {
        return awareness() * 0.5f;
    }
	
	public int attackProc( Char enemy, int damage, boolean blocked ) {
		return damage;
	}
	
	public int defenseProc( Char enemy, int damage, boolean blocked ) {
		return damage;
	}

    public int STR() {
        return 0;
    }

	public DamageType damageType() {
		return null;
	}

    public boolean ignoresAC() { return false; }

    public boolean blocksRanged() {
        return false;
    }

    public boolean immovable() {
        return false;
    }

	public float moveSpeed() {
		return ( buff( Levitation.class ) == null ? ( buff( Cripple.class ) == null ? baseSpeed : baseSpeed * 0.5f ) : baseSpeed * 1.5f );
	}

    public float awareness() {
        return buff( Confusion.class ) == null && buff( Blindness.class ) == null ? 1.0f : 0.0f ;
    }

    public float stealth() {
        return buff( Burning.class ) == null && buff( Light.class ) == null ? 1.0f : 0.0f ;
    }

    public boolean isRanged() {
        return false;
    }

    public boolean ignoresDistancePenalty(){
        return false;
    }

    public boolean isMagical() {
        return immunities().contains( DamageType.Body.class );
    }

    public boolean isHeavy() {
        return STR() > Dungeon.hero.STR();
    }

	public void damage( int dmg, Object src, DamageType type ) {
		
		if (HP <= 0) {
			return;
		}

        if( this instanceof Hero ){
            if( Dungeon.difficulty == Difficulties.EASY ) {
                dmg -= ( dmg / 2 + ( Random.Int(2) < dmg % 2 ? 1 : 0 ) );
            } else if( Dungeon.difficulty == Difficulties.IMPOSSIBLE ) {
                dmg += ( dmg / 2 + ( Random.Int(2) < dmg % 2 ? 1 : 0 ) );
            }

            Dungeon.hero.interrupt( "You were awoken by an attack!" );
        }

        int textColor = CharSprite.NEGATIVE;

        if( type != null ) {

            Class<? extends DamageType> typeClass = type.getClass();

            if (immunities().contains(typeClass)) {
                dmg = 0;
                textColor = CharSprite.NEUTRAL;
            } else if (resistances().contains(typeClass)) {
                dmg = dmg / 2 + Random.Int(dmg % 2 + 1);
                textColor = CharSprite.WARNING;
            }

            dmg = type.proc( this, dmg );
        }

        if( type != null && src instanceof Char ) {
            if (src instanceof Hero) {

                Hero hero = (Hero) src;

                Armour armor = hero.currentArmour;

                if (armor != null && armor.glyph instanceof Deflection && armor.bonus < 0 && Armour.Glyph.procced(armor.bonus)) {
                    ((Char)src).damage(Random.IntRange(dmg, dmg * 2), null, type);
                }

            } else if ( this instanceof Hero ) {
                Hero hero = (Hero)this;

                Armour armor = hero.currentArmour;

                if (armor != null && armor.glyph instanceof Deflection && armor.bonus > 0 && Armour.Glyph.procced(armor.bonus)) {
                    ((Char)src).damage( Random.IntRange(dmg, dmg * 2), null, type);
                }
            }
        }

		sprite.showStatus( textColor, Integer.toString( dmg ) );

        sprite.flash();

        HP -= dmg;

        if( src instanceof Char && !( type instanceof DamageType.Frost)  ) {
            Buff.detach(this, Frozen.class);
        }

        if( src instanceof Char && isCharmedBy( (Char)src ) ) {
            Buff.detach(this, Charm.class);
        }

		if ( !isAlive() ) {

			die(src, type);

		}
	}
	
	public void destroy() {
		HP = 0;
		Actor.remove(this);
		Actor.freeCell(pos);
	}

    public void die( Object src) {

        die(src, null);

    }

	public void die( Object src, DamageType dmg ) {
		destroy();

		sprite.die();
	}

    public boolean detected( Char ch ) {

//        float rnd1 = Random.Float();
//
//        float ste = ch.stealth();

//        float geo =  ( !ch.flying ? Dungeon.level.stealthModifier( ch.pos ) : 1.5f );
//
//        float awr = awareness();
//
//        int dist = distance(ch);
//        int rnd2 = Random.IntRange(1, dist + 1);
//
//        float ch1 = rnd1 * ste * geo;
//        float ch2 = awr / rnd2;
//
//        return ch1 < ch2;

        return Random.Float( ch.stealth() ) * ( !ch.flying ? Dungeon.level.stealthModifier( ch.pos ) : 1.5f )
                < Random.Float( awareness() * 2.0f ) / Math.sqrt( distance(ch) + 1 );

    }
	
	public boolean isAlive() {
		return HP > 0;
	}

    public boolean isDamagedOverTime() {
        for (Buff b : buffs) {
            if (b instanceof Burning
                || b instanceof Poison
                || b instanceof Ooze
                || b instanceof Bleeding
            ) {
                return true;
            }
        }
        return false;
    }

	@Override
	public void spend( float time ) {
		
		float timeScale = 1f;
		if (buff( Slow.class ) != null) {
			timeScale *= 0.5f;
		}
		if (buff( Speed.class ) != null) {
			timeScale *= 1.5f;
		}
		
		super.spend( time / timeScale );
	}
	
	public HashSet<Buff> buffs() {
		return buffs;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Buff> HashSet<T> buffs( Class<T> c ) {
		HashSet<T> filtered = new HashSet<T>();
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				filtered.add( (T)b );
			}
		}
		return filtered;
	}


    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffs( Class<T> c ) {

        float bonus = 1.0f;

        for (Buff b : buffs) {
            if ( c.isInstance( b )) {
                bonus += ((Ring.RingBuff)b).effect();
            }
        }

        return bonus;
    }

    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffsHalved( Class<T> c ) {

        float bonus = ringBuffs( c );

        return ( 1.0f + bonus ) / 2.0f;
    }

    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffsThirded( Class<T> c ) {

        float bonus = ringBuffs( c );

        return ( 2.0f + bonus ) / 3.0f;

    }

    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffsBaseZero( Class<T> c ) {

        return ringBuffs( c ) - 1.0f;

    }

	@SuppressWarnings("unchecked")
	public <T extends Buff> T buff( Class<T> c ) {
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				return (T)b;
			}
		}
		return null;
	}

    public boolean isBlocking() {
        for (Buff b : buffs) {
            if (b instanceof Guard) {
                return true;
            }
        }
        return false;
    }

    public boolean isOpenedTo( Char ch ) {
        int chID = ch.id();
        for (Buff b : buffs) {
            if (b instanceof Exposed && ((Exposed)b).object == chID) {
                return true;
            }
        }
        return false;
    }
	
	public boolean isCharmedBy( Char ch ) {
		int chID = ch.id();
		for (Buff b : buffs) {
			if (b instanceof Charm && ((Charm)b).object == chID) {
				return true;
			}
		}
		return false;
	}

    public boolean isCharmedBy( int chID ) {

        for (Buff b : buffs) {
            if (b instanceof Charm && ((Charm)b).object == chID) {
                return true;
            }
        }
        return false;
    }
	
	public boolean add( Buff buff ) {
		
		buffs.add(buff);
		Actor.add(buff);
		
		if (sprite != null) {
			if (buff instanceof Poison) {
				
				CellEmitter.center( pos ).burst( PoisonParticle.SPLASH, 5 );
				sprite.showStatus( CharSprite.NEGATIVE, "poisoned" );
				
			} else if (buff instanceof Amok) {
				
				sprite.showStatus( CharSprite.NEGATIVE, "amok" );

			} else if (buff instanceof Slow) {

				sprite.showStatus( CharSprite.NEGATIVE, "slowed" );
				
			} else if (buff instanceof MindVision) {
				
				sprite.showStatus( CharSprite.POSITIVE, "mind" );
				sprite.showStatus( CharSprite.POSITIVE, "vision" );
				
			} else if (buff instanceof Stun) {

				sprite.add( CharSprite.State.PARALYSED );
				sprite.showStatus( CharSprite.NEGATIVE, "stunned" );
				
			} else if (buff instanceof Terror) {
				
				sprite.showStatus( CharSprite.NEGATIVE, "frightened" );
				
			} else if (buff instanceof Ensnared) {
				
				sprite.showStatus( CharSprite.NEGATIVE, "ensnared" );
				
			} else if (buff instanceof Cripple) {

				sprite.showStatus( CharSprite.NEGATIVE, "crippled" );
				
			} else if (buff instanceof Bleeding) {

				sprite.showStatus( CharSprite.NEGATIVE, "bleeding" );
				
			} else if (buff instanceof Confusion) {

                sprite.showStatus( CharSprite.NEGATIVE, "dizzy" );

//            } else if (buff instanceof Withered) {
//
//                sprite.showStatus(CharSprite.NEGATIVE, "weakened");

            } else if (buff instanceof Sleep) {
				sprite.idle();
			}  else if (buff instanceof Burning) {
                sprite.add( CharSprite.State.BURNING );
            } else if (buff instanceof Withered) {
                sprite.add( CharSprite.State.WITHERED );
            } else if (buff instanceof Levitation) {
                sprite.showStatus( CharSprite.POSITIVE, "levitating" );
                sprite.add( CharSprite.State.LEVITATING );
            } else if (buff instanceof Frozen) {
                sprite.showStatus(CharSprite.NEGATIVE, "frozen");
                sprite.add(CharSprite.State.FROZEN);
			} else if (buff instanceof Invisibility) {
                sprite.showStatus( CharSprite.POSITIVE, "invisible" );
				sprite.add( CharSprite.State.INVISIBLE );
			} else if (buff instanceof Enraged) {
//                sprite.showStatus(CharSprite.POSITIVE, "enraged");
                sprite.add(CharSprite.State.ENRAGED);
            } else if (buff instanceof Charm) {
                sprite.showStatus(CharSprite.POSITIVE, "charmed");
                sprite.add( CharSprite.State.CHARMED );
            } else if (buff instanceof ForceField) {
                sprite.showStatus(CharSprite.POSITIVE, "shield");
                sprite.add(CharSprite.State.PROTECTION);
            } else if (buff instanceof UnholyArmor) {
                sprite.showStatus(CharSprite.POSITIVE, "unholy armor");
                sprite.add(CharSprite.State.UNHOLYARMOR);
            } else if (buff instanceof Exposed) {
                sprite.showStatus( CharSprite.NEUTRAL, TXT_EXPOSED );
            }
		}

//        if( Dungeon.hero == this ) {
//            BuffIndicator.refreshHero();
//        }

        return true;
	}
	
	public void remove( Buff buff ) {
		
		buffs.remove(buff);
		Actor.remove(buff);

        if (buff instanceof Burning) {
            sprite.remove( CharSprite.State.BURNING );
        } else if (buff instanceof Charm) {
            sprite.remove( CharSprite.State.CHARMED );
        } else if (buff instanceof Withered) {
            sprite.remove( CharSprite.State.WITHERED );
        } else if (buff instanceof Levitation) {
			sprite.remove( CharSprite.State.LEVITATING );
		} else if (buff instanceof Invisibility && invisible <= 0) {
			sprite.remove( CharSprite.State.INVISIBLE );
		} else if (buff instanceof Stun) {
			sprite.remove( CharSprite.State.PARALYSED );
		} else if (buff instanceof Frozen) {
            sprite.remove( CharSprite.State.FROZEN );
        } else if (buff instanceof Enraged) {
            sprite.remove( CharSprite.State.ENRAGED );
        } else if (buff instanceof Challenge) {
            sprite.remove( CharSprite.State.CHALLENGE );
        } else if (buff instanceof Light) {
            sprite.remove( CharSprite.State.ILLUMINATED );
        } else if (buff instanceof ForceField) {
            sprite.remove( CharSprite.State.PROTECTION );
        } else if (buff instanceof UnholyArmor) {
            sprite.remove( CharSprite.State.UNHOLYARMOR);
        }
    }
	
	public void remove( Class<? extends Buff> buffClass ) {
		for (Buff buff : buffs(buffClass)) {
			remove(buff);
		}
	}
	
	@Override
	protected void onRemove() {
		for (Buff buff : buffs.toArray(new Buff[0])) {
			buff.detach();
		}
	}
	
	public void updateSpriteState() {
		for (Buff buff:buffs) {
            if (buff instanceof Burning) {
                sprite.add(CharSprite.State.BURNING);
            } else if (buff instanceof Charm) {
                sprite.add(CharSprite.State.CHARMED);
            } else if (buff instanceof Withered) {
                sprite.add(CharSprite.State.WITHERED);
            } else if (buff instanceof Levitation) {
				sprite.add(CharSprite.State.LEVITATING);
			} else if (buff instanceof Invisibility) {
				sprite.add( CharSprite.State.INVISIBLE );
			} else if (buff instanceof Stun) {
				sprite.add( CharSprite.State.PARALYSED );
			} else if (buff instanceof Frozen) {
				sprite.add( CharSprite.State.FROZEN );
			} else if (buff instanceof Light) {
                sprite.add( CharSprite.State.ILLUMINATED );
            } else if (buff instanceof Enraged) {
                sprite.add( CharSprite.State.ENRAGED );
            } else if (buff instanceof Challenge) {
                sprite.add( CharSprite.State.CHALLENGE );
            } else if (buff instanceof ForceField) {
                sprite.add( CharSprite.State.PROTECTION );
            } else if (buff instanceof UnholyArmor) {
                sprite.add( CharSprite.State.UNHOLYARMOR);
            }
		}
	}

	public void move( int step ) {
		
		if (Level.adjacent( step, pos ) && Random.Int( 2 ) == 0 && ( ( buff( Confusion.class ) != null
            || this instanceof Mob && buff( Blindness.class ) != null ) ) ) {

			step = pos + Level.NEIGHBOURS8[Random.Int( 8 )];

			if ( Level.solid[step] || Actor.findChar( step ) != null ) {
				return;
			}
		}
		
		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}

        Actor.freeCell( pos );

		pos = step;
		
		if (Dungeon.level.map[pos] == Terrain.DOOR_CLOSED) {
			Door.enter( pos );
		}
		
		if (this != Dungeon.hero) {
			sprite.visible = Dungeon.visible[pos];
		}

        Actor.occupyCell( this );

        Dungeon.level.press(pos, this);

        moving = true;
	}
	
	public int distance( Char other ) {
		return Level.distance( pos, other.pos );
	}
	
	public void onMotionComplete() {
		next();
	}
	
	public void onAttackComplete() {
		next();
	}

    public void onCastComplete() {
        next();
    }


    public void onComplete() {
        next();
    }
	
	public void onOperateComplete() {
		next();
	}
	
	public HashSet<Class<? extends DamageType>> resistances() {
		return new HashSet<>();
	}
	
	public HashSet<Class<? extends DamageType>> immunities() {
		return new HashSet<>();
	}
}
