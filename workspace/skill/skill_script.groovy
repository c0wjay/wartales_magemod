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
            break;
        }
    }
    skill.target.gainsHealth( ceil( skill.target.stats.health * ( min(skill.unit.stats.willpower, 50)/50 ) * (recovery/100) * randInt(12,20)/16 ) , null);
    if( skill.level == 2 ) {
        for( s in skill.target.getAllStatus() ) {
            if( s.isMalus )
                s.cancel();
        }
    }
}


// RangeHeal (BeastMaster, FirstAid, PoisonFlask 참고)
function onSkill() {
    playAttack();
    var recovery = vars.value1;
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedRecovery ) {
            recovery += ( vars.value1 * s.count / 10 );
            break;
        }
    }
    var will = min(skill.unit.stats.willpower, 50);
	for( t in skill.getTargets() ) {
        if( t.target.side == skill.unit.side ) {
            t.target.gainsHealth( ceil( t.target.stats.health * ( will/50 ) * (recovery/100) * randInt(12,20)/16 ) , null);
            if( skill.level == 2 && randomDice(will) == 3 ) {
                t.target.addStatus(Status.Protection);
            }
        }
	}
}
function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice > 90 ) return 5;
    if ( dice > 80 ) return 4;
    if ( dice > 60 ) return 3;
    if ( dice > 30 ) return 2;
    else return 1;
}


// GroupHeal (BeastMaster, Ovation 참고)
function onSkill() {
    play();
    var recovery = vars.value1;
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedRecovery ) {
            recovery += ( vars.value1 * s.count / 10 );
            break;
        }
    }
    @sync for( u in getAllies(skill.unit) ) {
        u.gainsHealth(ceil( u.stats.health * min(skill.unit.stats.willpower, 50)/50 * (recovery/100) ), null);
        if( skill.level == 2 ) {
            var armorRecovery = ceil ( u.stats.armor * min(skill.unit.stats.willpower, 50)/100 * (recovery/100) );
            u.armor = min(u.armor + armorRecovery, u.stats.armor);
        }
    }
}


// FearVoice
function onSkill() {
    playAttack();
    var will = min(skill.unit.stats.willpower, 50);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedCurse ) {
            will += ( s.count * 2 );
            break;
        }
    }
    for( t in skill.getTargets() ) {
        var num = randomDice( max(will - vars.value1, 0) );
        t.target.addStatus(Status.Terror, num);
        if( skill.level == 2 ) {
            var num2 = randomDice( max(will - vars.value2, 0) );
            if( num2 > 2 ) {
                t.target.addStatus(Status.Immobile);
            }
        }
    }
}
function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice > 90 ) return 5;
    if ( dice > 80 ) return 4;
    if ( dice > 60 ) return 3;
    if ( dice > 30 ) return 2;
    else return 1;
}


// DeathScent
function onSkill() {
    playAttack();
    var will = min(skill.unit.stats.willpower, 50);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedCurse ) {
            will += ( s.count * 2 );
            break;
        }
    }
    for( t in skill.getTargets() ) {
            var num = randomDice( max(will - vars.value1, 0) );
            if ( num == 1 ) t.target.addStatus(Status.Slowdown);
            if ( num == 2 ) t.target.addStatus(Status.Fever, 3);
            if ( num == 3 || num == 4 ) t.target.addStatus(Status.Weakening);
            if ( num == 5 ) t.target.addStatus(Status.Fever, 5);
            if( skill.level == 2 ) {
                var num2 = randomDice( max(will - vars.value2, 0) );
                if( num2 > 3 ) {
                    t.target.addStatus(Status.Stun);
                } else {
                    t.target.addStatus(Status.Fever, 2);
                }
            }
    }
}
function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice > 90 ) return 5;
    if ( dice > 80 ) return 4;
    if ( dice > 60 ) return 3;
    if ( dice > 30 ) return 2;
    else return 1;
}


// PainLess
function onSkill() {
    playAttack();
    var will = min(skill.unit.stats.willpower, 50);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedCurse ) {
            will += ( s.count * 2 );
            break;
        }
    }
    var num = randomDice( max(will - vars.value1, 0) );
    if ( num < 3 || num == 4 ) {
        skill.target.addStatus(Status.Enervate, randomDice(will));
    }
    if ( num == 3 ) skill.target.addStatus(Status.Berserk, 1);
    if ( num == 5 ) skill.target.addStatus(Status.BrothersFury, 1);
    if( skill.level == 2 ) {
        var num2 = randomDice( max(will - vars.value2, 0) );
        if( num2 > 3 ) {
            if (!skill.target.getPassiveSkills().contains(Skill.Immortal)) {
                skill.target.addTemporarySkill(Skill.Immortal);
            } else {
                skill.target.addStatus(Status.Enervate, 3);
            }
        }
    }
    spawnFx();
}
function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice > 90 ) return 5;
    if ( dice > 80 ) return 4;
    if ( dice > 60 ) return 3;
    if ( dice > 30 ) return 2;
    else return 1;
}


// MagicMissile
function onEval(a) {
    a.dmg += calculateDamage(a.target);
}
function calculateDamage(t) {
    var target_hp = t.health + t.armor;
    if (target_hp == null) {
        target_hp = t.stats.health;
    }
    var damage = (target_hp * vars.value1) + vars.value2; 
    damage = damage * skill.unit.stats.willpower / 100;
    return ceil(damage);
}


// EarthQuake
function onEval(a) {
    a.dmg += calculateDamage(a.target);
}
function calculateDamage(t) {
    var target_hp = t.stats.health + t.stats.armor;
    var damage = (target_hp * vars.value1) + vars.value2;
    damage = damage * (skill.unit.stats.willpower / 100);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedElement ) {
            damage += ( damage * s.count / 10 );
            break;
        }
    }
    return ceil(damage);
}


// FireBall
function onEval(a) {
    a.dmg += calculateDamage(a.target);
}
function onZoneHit() {
    createSkillZone(Skill.FireZone);
}
function calculateDamage(t) {
    var target_hp = t.stats.health + t.stats.armor;
    var damage = (target_hp * vars.value1) + vars.value2;
    damage = damage * (skill.unit.stats.willpower / 100);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedElement ) {
            damage += ( damage * s.count / 10 );
            break;
        }
    }
    return ceil(damage);
}

// ThunderStorm
function onEval(a) {
    a.dmg += calculateDamage(a.target);
}
function onPostSkill() {
    if ( skill.level == 2 )
        cast(Skill.ThunderStormZone, { skill : skill }, skill);
}
function calculateDamage(t) {
    var target_hp = t.stats.health;
    var damage = (target_hp * vars.value1) + vars.value2;
    damage = damage * (skill.unit.stats.willpower / 100);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedElement ) {
            damage += ( damage * s.count / 10 );
            break;
        }
    }
    return ceil(damage);
}


// ThunderStormZone
function onSkill() {
    createAreaEffect(\"Infinite\", 0);
}

function onZoneIn( a ) {
    for( u in a.getTargets() )
        u.addStatus(Status.Immobile);
    a.remove();
}

"



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

// SummonBeast
function onSkill() {
    play();
    var will = min(skill.unit.stats.willpower, 50);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedNecromancy ) {
            will += ( s.count * 2 );
            break;
        }
    }
    var previous = getAllies(skill.unit);
    var dice = [];
    dice.push(randomDice(will));
    dice.push(randomDice(max(will - vars.value1, 0)));

    var tab = [];
    if (skill.level == 2) {
        tab.push(UnitClass.Swamoar);
        tab.push(UnitClass.SnowWolf);
        tab.push(UnitClass.SnowAlpha);
        tab.push(UnitClass.WhiteBear);
    } else {
        tab.push(UnitClass.Boar);
        tab.push(UnitClass.Wolf);
        tab.push(UnitClass.Alpha);
        tab.push(UnitClass.Bear);
    }

    var idx = dice[0] -1;
    var num = (dice[1]+2)/2;
    if (idx ==3) {
        spawnRenfort(tab[3], (num+1)/2, false);
    } else if (idx == 4) {
        spawnRenfort(tab[0], num, false);
    } else {
        spawnRenfort(tab[idx], num, false);
    }
    for (u in getAllies(skill.unit)) {
        if (!previous.contains(u)) {
            u.movePoints += 16;
        }
    }
}
function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice > 90 ) return 5;
    if ( dice > 80 ) return 4;
    if ( dice > 60 ) return 3;
    if ( dice > 30 ) return 2;
    else return 1;
}


// SummonUndead
function onBeginBattle() {
    vars.allowed = true;
    vars.value2 = 2;
}
function onBeginAction() {
    vars.allowed = false;
    if( vars.value2 >= 2 ){
        vars.allowed = true;
    }
}
function onEval(a) {
    if( vars.value2 < 2 ){
        dontAllow();
    }
}
function onSkill() {
    play();
    var will = min(skill.unit.stats.willpower, 50);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedNecromancy ) {
            will += ( s.count * 2 );
            break;
        }
    }
    var previous = getAllies(skill.unit);
    var dice = [];
    dice.push(randomDice(will));
    dice.push(randomDice(max(will - vars.value1, 0)));

    var tab = [];
    if (skill.level == 2) {
        tab.push(UnitClass.PlaguedRat);
        tab.push(UnitClass.Plagueridden);
        tab.push(UnitClass.Nightmare);
        tab.push(UnitClass.SnowCrawler);
    } else {
        tab.push(UnitClass.Molerat);
        tab.push(UnitClass.GhostBoar);
        tab.push(UnitClass.GhostWolf);
        tab.push(UnitClass.Crawler);
    }

    var idx = dice[0] -1;
    if (idx == 4) {
        spawnRenfort(tab[0], dice[1], false);
    } else {
        spawnRenfort(tab[idx], dice[1], false);
    }
    for (u in getAllies(skill.unit)) {
        if (!previous.contains(u)) {
            u.movePoints += 16;
        }
    }
    vars.value2 = 0;
}
function onEndRound() {
    vars.value2++;
}
function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice > 90 ) return 5;
    if ( dice > 80 ) return 4;
    if ( dice > 60 ) return 3;
    if ( dice > 30 ) return 2;
    else return 1;
}


// SummonChampion
function onBeginBattle() {
    vars.allowed = true;
}
function onEval(a) {
    if( !vars.allowed ){
        dontAllow();
    }
}
function onSkill() {
    play();
    var will = min(skill.unit.stats.willpower, 50);
    for ( s in skill.unit.getAllStatus() ) {
        if( s.kind == Status.ReinforcedNecromancy ) {
            will += ( s.count * 2 );
            break;
        }
    }
    var previous = getAllies(skill.unit);
    var dice = [];
    dice.push(randomDice(max(will - vars.value1, 0)));
    dice.push(randomDice(will));

    var tab = [];
    var idx = [];
    if (dice[0] == 2) {
        tab.push(UnitClass.PuzzleMan);
        idx.push(1);
    } else if (dice[0] == 3) {
        if (skill.level == 2 && dice[1] > 3 ) {
            tab.push(UnitClass.Kaghal);
            idx.push(1);
        } else {
            tab.push(UnitClass.Smot);
            idx.push(1);
        }
    } else if (dice[0] == 4) {
        if (skill.level == 2 && dice[1] > 3 ) {
            tab.push(UnitClass.CrawlerChampion);
            idx.push(1);
        } else {
            tab.push(UnitClass.Kogo);
            tab.push(UnitClass.Toro);
            idx.push(1);
            idx.push(1);
        }
    } else if (dice[0] == 5) {
        if (skill.level == 2 && dice[1] > 3 ) {
            tab.push(UnitClass.ChristophGluck);
            idx.push(1);
        } else {
            tab.push(UnitClass.Bionn);
            idx.push(1);
        }
    } else {
        tab.push(UnitClass.Mobster);
        idx.push(dice[1]);
    }

    var i = 0;
    while(i < min(tab.length, idx.length)) {
        if (idx[i] != 0) {
            spawnRenfort(tab[i], idx[i], false);
        }
        i++;
    }
    for (u in getAllies(skill.unit)) {
        if (!previous.contains(u)) {
            u.movePoints += 16;
        }
    }
    vars.allowed = false;
}
function randomDice (w) {
    var dice = randInt(w, 100);
    if ( dice > 90 ) return 5;
    if ( dice > 80 ) return 4;
    if ( dice > 60 ) return 3;
    if ( dice > 30 ) return 2;
    else return 1;
}



// SummonChampion Test
function onBeginBattle() {
    vars.allowed = true;
}
function onEval(a) {
    if( !vars.allowed ){
        dontAllow();
    }
}
function onSkill() {
    play();

    var tab = [UnitClass.Kogo, UnitClass.Toro, UnitClass.Kaghal, UnitClass.ChristophGluck, UnitClass.Bionn];
    var idx = [1, 1, 1, 1, 1];

    var i = 0;
    while(i < min(tab.length, idx.length)) {
        if (idx[i] != 0) {
            spawnRenfort(tab[i], idx[i], false);
        }
        i++;
    }
    vars.allowed = false;
}