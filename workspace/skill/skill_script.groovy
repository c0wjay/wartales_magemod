// MasterBall
function onBeginAction() {
    vars.allowed = false;
    for( u in getFoes(skill.unit) )
        if( canCaptureTarget(u) ) {
            vars.allowed = true;
            break;
        }
}

function onEval(a) {
    if( !canCaptureTarget(a.target) ){
        dontAllow();
    }
}

function onSkill() {
    for (t in skill.getTargets()){
        captureTarget(t);
    }
}

function canCaptureTarget(t){
    return true;
}


// TargetHeal (TerrorLink, DopingSshot 참고)
function onSkill() {
    play();
    var recovery = vars.value1;
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedRecovery ) {
            recovery += ( vars.value1 * s.count / 10 );
        }
    }
    skill.target.gainsHealth( ceil( skill.target.stats.health * ( min(skill.unit.stats.willpower, 50)/50 ) * (recovery/100) * randInt(12,20)/16 ) , null);
    if( skill.level == 2 ) {
        var armorRecovery = ceil ( skill.target.stats.armor * min(skill.unit.stats.willpower, 50)/100 * (recovery/75) * randInt(12,20)/16 );
        skill.target.armor = min(skill.target.armor + armorRecovery, skill.target.stats.armor);
    }
}


// RangeHeal (BeastMaster, FirstAid, PoisonFlask 참고)
function onSkill() {
    playAttack();
    var recovery = vars.value1;
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedRecovery ) {
            recovery += ( vars.value1 * s.count / 10 );
        }
    }
    var will = min(skill.unit.stats.willpower, 50);
	for( t in skill.getTargets() ) {
        if( t.target.side == skill.unit.side ) {
            t.target.gainsHealth( ceil( t.target.stats.health * ( will/50 ) * (recovery/100) * randInt(12,20)/16 ) , null);
            if( skill.level == 2 && randomDice(will) == 3 ) {
                t.target.addStatus(Status.Protection, vars.value2, true);
            }
            spawnFx();
        }
	}
}

function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice <= 30 ) return 1;
    if ( dice <= 60 ) return 2;
    if ( dice <= 80 ) return 3;
    if ( dice <= 90 ) return 4;
    else return 5;
}


// GroupHeal (BeastMaster, Ovation 참고)
function onSkill() {
    play();
    var recovery = vars.value1;
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedRecovery ) {
            recovery += ( vars.value1 * s.count / 10 );
        }
    }
    @sync for( u in getAllies(skill.unit) ) {
        u.gainsHealth(ceil( u.stats.health * min(skill.unit.stats.willpower, 50)/50 * (recovery/100) ), null);
    }
    spawnFx();
}


// FearVoice
function onSkill() {
    playAttack();
    var will = min(skill.unit.stats.willpower, 50);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedCurse ) {
            will += ( s.count * 2 );
        }
    }
    for( t in skill.getTargets() ) {
        if( t.target.side != skill.unit.side ) {
            var num = randomDice( max(will - vars.value1, 0) );
            t.target.addStatus(Status.Terror, num);
            if( skill.level == 2 ) {
                var num2 = randomDice( max(will - vars.value2, 0) );
                if( num2 > 2 ) {
                    t.target.addStatus(Status.Immobile, 1, true);
                }
            }
        }
    }
}

function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice <= 30 ) return 1;
    if ( dice <= 60 ) return 2;
    if ( dice <= 80 ) return 3;
    if ( dice <= 90 ) return 4;
    else return 5;
}


// DeathScent
function onSkill() {
    playAttack();
    var will = min(skill.unit.stats.willpower, 50);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedCurse ) {
            will += ( s.count * 2 );
        }
    }
    for( t in skill.getTargets() ) {
        if( t.target.side != skill.unit.side ) {
            var num = randomDice( max(will - vars.value1, 0) );
            if ( num == 2 ) t.target.addStatus(Status.Fragility, 2, true);
            if ( num == 3 ) t.target.addStatus(Status.Fracture, 1, true);
            if ( num == 4 ) t.target.addStatus(Status.Weakening, 2, true);
            if ( num == 5 ) t.target.addStatus(Status.Bruise, 1, true);
            if( skill.level == 2 ) {
                var num2 = randomDice( max(will - vars.value2, 0) );
                if( num2 > 3 ) {
                    t.target.addStatus(Status.Stun, 2, true);
                }
            }
        }
    }
}

function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice <= 30 ) return 1;
    if ( dice <= 60 ) return 2;
    if ( dice <= 80 ) return 3;
    if ( dice <= 90 ) return 4;
    else return 5;
}


// PainLess
function onSkill() {
    playAttack();
    var will = min(skill.unit.stats.willpower, 50);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedCurse ) {
            will += ( s.count * 2 );
        }
    }
    var num = randomDice( max(will - vars.value1, 0) );
    if ( num == 2 || num == 4 ) {
        skill.target.addStatus(Status.Enervate, randomDice(will));
    }
    if ( num == 3 ) skill.target.addStatus(Status.Berserk, 1, true);
    if ( num == 5 ) skill.target.addStatus(Status.BrothersFury, 1, true);
    if( skill.level == 2 ) {
        var num2 = randomDice( max(will - vars.value2, 0) );
        if( num2 == 5 ) {
            skill.target.addStatus(Status.Arena_Willforce, 2, true);
        }
    }
}

function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice <= 30 ) return 1;
    if ( dice <= 60 ) return 2;
    if ( dice <= 80 ) return 3;
    if ( dice <= 90 ) return 4;
    else return 5;
}


// MagicMissile
function onEval(a) {
    a.dmg += calculateDamage(a);
}
function calculateDamage(t) {
    var target_hp = t.health + t.armor;
    var damage = ceil(target_hp * skill.unit.stats.willpower * vars.value1 / 100);
    return damage;
}

// MagicMissileTest
function onEval(a) { a.dmg += 100; }

function onEval(a) { a.dmg = 100; }


// function onEval(a) {
//     a.dmg += calculateDamage(a);
// }
// function onSkill() {
//     playAttack();
//     skill.target.damages(skill, calculateDamage(skill.target));
// }
// function calculateDamage(t) {
//     var target_hp = t.health + (t.armor >= 1 ? t.armor : 0);
//     var damage = floor(target_hp * skill.unit.stats.willpower * vars.value1 / 100);
//     return min(damage, t.health - 1);
// }


// G2Arena1RuleLifeLinked
function onBeginAction() {
    for(t in getFoeUnits()) {
        if(t.health == vars.value) {
            if(!t.hasStatus(Status.Arena_Willforce)) {
                t.addStatus(Status.Arena_Willforce);
                spawnFx(t);
            }
        }
        else {
            t.cancelStatus(Status.Arena_Willforce);
        }
    }

    for(u in getFoeUnits()) {
        if(!u.hasStatus(Status.Arena_Willforce)) {
            return;
        }
    }
    win(true);
}


// FearVoiceTest
function onSkill() {
    play();
    var will = min(skill.unit.stats.willpower, 50);
    for( t in skill.getTargets() ) {
        if( t.target.side != skill.unit.side ) {
            var num = randomDice( 50 );
            t.target.addStatus(Status.Terror, num);
            var num2 = randomDice( 50 );
            if( num2 > 2 ) {
                t.target.addStatus(Status.Immobile, 1);
            }
        }
    }
}
function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice <= 30 ) return 1;
    if ( dice <= 60 ) return 2;
    if ( dice <= 80 ) return 3;
    if ( dice <= 90 ) return 4;
    else return 5;
}


// DeathScentTest
function onSkill() {
    play();
    var will = 50;
    for( t in skill.getTargets() ) {
        if( t.target.side != skill.unit.side ) {
            var num = randomDice( max(will - vars.value1, 0) );
            if ( num == 2 ) t.target.addStatus(Status.Fragility, 2, true);
            if ( num == 3 ) t.target.addStatus(Status.Fracture, 1, true);
            if ( num == 4 ) t.target.addStatus(Status.Weakening, 2, true);
            if ( num == 5 ) t.target.addStatus(Status.Bruise, 1, true);
            t.target.addStatus(Status.Stun, 2, true);
        }
    }
}
function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice <= 30 ) return 1;
    if ( dice <= 60 ) return 2;
    if ( dice <= 80 ) return 3;
    if ( dice <= 90 ) return 4;
    else return 5;
}


// PainLessTest
function onSkill() {
    play();
    var will = 50;
    var num = randomDice( max(will - vars.value1, 0) );
    if ( num == 2 || num == 4 ) {
        skill.target.addStatus(Status.Enervate, randomDice(will));
    }
    if ( num == 3 ) skill.target.addStatus(Status.Berserk, 1, true);
    if ( num == 5 ) skill.target.addStatus(Status.BrothersFury, 1, true);
    skill.target.addStatus(Status.Arena_Willforce, 2, true);
}
function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice <= 30 ) return 1;
    if ( dice <= 60 ) return 2;
    if ( dice <= 80 ) return 3;
    if ( dice <= 90 ) return 4;
    else return 5;
}


// AutoTeleportation
function onDamageTaken(a) {
    var arr = [];
    for(u in getAllies(skill.unit)) {
        if( getDistance(u, skill.unit) <= vars.value1 && !u.isEngaged() && u.canMove() ) {
            arr.push(u);
        }
    }
    if( arr.length > 0 ) {
        var target = arr[randInt(0, arr.length-1)];
        skill.unit.swapPositionWith(target, 0.2);
        target.addStatus( Status.Dodge );
        a.unit.addStatus( Status.Confus, 1 );
    }
}


// Teleportation
function onEval(a) {
    if( !a.target.canMove() ) {
        dontAllow();
    }
}
function onSkill() {
    if( skill.target.canMove() ) {
        skill.unit.swapPositionWith(skill.target, 0.2);
        skill.target.addStatus( Status.Dodge );
    }
}


// Meditation
function onSkill() {
    skill.unit.gainsActionPoint(vars.value1);
}


// PriestPath
function onBeginBattle() {
    vars.start = false;
}
function onBeginAction() {
    if ( vars.start == false ) {
        skill.unit.addStatus(Status.ReinforcedRecovery, vars.value1);
        vars.start = true;
    }
}

// WarlockPath
function onBeginBattle() {
    vars.start = false;
}
function onBeginAction() {
    if ( vars.start == false ) {
        skill.unit.addStatus(Status.ReinforcedCurse, vars.value1);
        vars.start = true;
    }
}

// SorcererPath
function onBeginBattle() {
    vars.start = false;
}
function onBeginAction() {
    if ( vars.start == false ) {
        skill.unit.addStatus(Status.ReinforcedElement, vars.value1);
        vars.start = true;
    }
}

// Renfort (네크 참고용)
function onSkill() {
    play();
    var tab = [UnitClass.Mobster, UnitClass.Poacher, UnitClass.Marauder, UnitClass.MischiefMaker];
    for(u in getAllies(skill.unit)) {
        tab.remove(u.kind);
    }
    for(t in tab)
        spawnRenfort(t, 1, false);
}

// MasteredWhirlwind
function onEval(a) {
    a.dmg += ceil(skill.value*skill.getTargetsCount()-skill.value);
}
function onHit(a) {
    a.target.pushback({ unit : skill.unit }, getPushback(a));
}
function getPushback( a ) {
    return vars.value1;
}

// ToxicBlade
function onEval(a) {
    for( s in a.target.getAllStatus() ) {
        if( s.kind == Status.Poison ) {
            a.baseDamageBonus += s.count * vars.value1;
        }
    }
}
function onDamage(a) {
    for( st in a.target.getAllStatus() ) {
        if( st.kind == Status.Poison ) {
            st.cancel();
        }
    }
}


// EquipedWithIncendiaryFlaskZone
function onSkill() {
    createAreaEffect(\"Immediate\", 1, { skillId: Skill.FireZone });
}

"

// FireZone
function onZoneIn(a) {
    a.target.addStatus(Status.Burning);
}

function onZoneMoveEnd(a) {
    a.target.addStatus(Status.Burning);
}

// LightningZone


// CelestiumLightning


// PestiferedFromCelling

// RockSlideZone