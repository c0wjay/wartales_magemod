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
    skill.target.gainsHealth( ceil( skill.target.stats.health * ( min(skill.unit.stats.willpower, 50)/50 ) * (vars.value1/100) * randInt(12,20)/16 ) , null);
    if( skill.level == 2 ) {
        var armorRecovery = ceil ( skill.target.stats.armor * min(skill.unit.stats.willpower, 50)/100 * randInt(12,20)/16 );
        skill.target.armor = min(skill.target.armor + armorRecovery, skill.target.stats.armor);
    }
}


// RangeHeal (BeastMaster, FirstAid, PoisonFlask 참고)
function onSkill() {
    playAttack();
    var will = min(skill.unit.stats.willpower, 50);
	for( t in skill.getTargets() ) {
        if( t.target.side == skill.unit.side ) {
            t.target.gainsHealth( ceil( t.target.stats.health * ( will/50 ) * (vars.value1/100) * randInt(12,20)/16 ) , null);
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
    @sync for( u in getAllies(skill.unit) ) {
        u.gainsHealth(ceil( u.stats.health * min(skill.unit.stats.willpower, 50)/50 * (vars.value1/100) ), null);
    }
    spawnFx();
}


// FearVoice
function onSkill() {
    playAttack();
    var will = min(skill.unit.stats.willpower, 50);
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
function onSkill() {
    skill.target.damages(skill, ceil((skill.target.health + skill.target.armor) * skill.unit.stats.willpower * vars.value1 / 100), true);
}


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


// GluckTraitorSwap
function onDamageDealt(a) {
    if(!a.unit.isEngaged())
        return;

    var engagedUnit = a.unit.engagedUnit;

    var tab = [];
    for(u in getAllies(skill.unit)) {
        if( getDistance(u, skill.unit) <= vars.value1 && !u.isEngaged() && !u.isAnimal && u.canMove() ) {
            tab.push(u);
        }
    }
    if(tab.length > 0) {
        var target = tab[randInt(0, tab.length-1)];

        a.unit.swapPositionWith(target, 0.2);

        target.engage(engagedUnit);
        if( target != skill.unit ) {
            engagedUnit.opportunityAttack(target, skill);
        }
    }
    else {
        a.unit.swapPositionWith(engagedUnit, 0.2);
        a.unit.engage(engagedUnit);
    }
}


// Intervention
function onEval(a) {
    if( !a.target.isEngaged() || a.target.isAnimal || !a.target.canMove() ) {
        dontAllow();
    }
}

function onSkill() {
    var target = skill.target;
    var prevEngaged = target.engagedUnit;
    target.disengage(false);

    var prevPos = target.getPosition();
    var currentPos = skill.unit.getPosition();
    skill.unit.swapPositionWith(target, 0);

    skill.unit.engage(prevEngaged);
    skill.unit.opportunityAttack(prevEngaged, skill);
}


// EquipedWithIncendiaryFlaskZone


// PriestPath
function onBeginBattle() {
skill.unit.addStatusPersist(Status.ReinforcedRecovery, skill);
}

// WarlockPath
function onBeginBattle() {
skill.unit.addStatusPersist(Status.ReinforcedCurse, skill);
}

// SorcererPath
function onBeginBattle() {
skill.unit.addStatusPersist(Status.ReinforcedElement, skill);
}