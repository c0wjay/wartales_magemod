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


// MagicMissile
