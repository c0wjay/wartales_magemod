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
	for( t in skill.getTargets() ) {
        if( t.target.side == skill.unit.side ) {
            t.target.gainsHealth( ceil( t.target.stats.health * ( min(skill.unit.stats.willpower, 50)/50 ) * (vars.value1/100) * randInt(12,20)/16 ) , null);
            if( skill.level == 2 && randInt(0,3) == 3 ) {
                t.target.addStatus(Status.Protection, vars.value2, true);
            }
            spawnFx();
        }
	}
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
    play();
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
    play();
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