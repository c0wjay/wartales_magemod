//Corrupt
function onBeginAction() {
    vars.allowed = false;
    for( u in getFoes(skill.unit) )
        if( skill.unit.canCorrupt(u) ) {
            vars.allowed = true;
            break;
        }
}
    
function onEval(s) {
    if( !skill.unit.canCorrupt(s.target) ) dontAllow();
}

function onSkill() {
    corrupt();
}



// KnockOut
function onBeginAction() {
    vars.allowed = false;
    for( u in getFoes(skill.unit) )
        if( canCaptureTarget(u) ) {
            vars.allowed = true;
            break;
        }
}

function onEval(s) {
    if( !canCaptureTarget(s.target) ){
        dontAllow();
    }
}

function onSkill() {
    capture();
}


@sync function canCaptureTarget(t){
    var canCapture = skill.unit.canCapture(t, true);
    var canUseBelt = t.isAnimal && !t.isGhost && skill.unit.hasSkill(Skill.BeltAccHunt);
    return canCapture && !canUseBelt;
}

//BeltAccHunt
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
    return t.isAnimal && !t.isGhost && skill.unit.canCapture(t, false);
}


// RandomTerror
function onEndTurn() {
    var arr = [];
    for( u in getFoes(skill.unit) ) {
        arr.push(u);
    }
    if( arr.length > 0 ) {
        arr[randInt(0,arr.length-1)].addStatus(Status.Terror, 5);
    }
}


// TerrorLink
function onSkill() {
    play();
    var currenttarget = null;
    for( t in skill.getTargets() ) {
        currenttarget = t.target;
        t.target.damages(skill, skill.value, true);
    }
    var arr = [];
    for( u in getFoes(skill.unit) ) {
        if( !u.hasStatus(Status.TerrorLink) && u != currenttarget ) {
            arr.push(u);
        }
    }
    if( arr.length > 0 ) {
        currenttarget.addStatus(Status.TerrorLink);
        arr[randInt(0,arr.length-1)].addStatus(Status.TerrorLink);
    }
}


// DopingSshot
function onSkill() {
	if( skill.target.side != skill.unit.side ) {
		attack();
		skill.target.addStatus(Status.Vulnerability);
	} else {
		play();
		skill.target.gainsHealth( ceil(skill.target.stats.health*vars.value1/100) , null);
		skill.target.addStatus(Status.Fury);
	}
}

// Ovation
function onSkill() {
    play();
    @sync for( u in getAllies(skill.unit)) {
        if( u.isEngaged() ) {
            u.addStatus(Status.Riposte);
        }
        if( skill.level == 2 && !u.isEngaged() ) {
            u.addStatus(Status.Inspiration, vars.value1, true);
        }
    }
}


// TargetHeal (TerrorLink, DopingSshot 참고)
function onSkill() {
    play();
    skill.target.gainsHealth( ceil(skill.target.stats.health*vars.value1/100) , null);
}


// TotalHeal (BeastMaster, Ovation 참고)
function onSkill() {
    play();
	@sync for( u in getAllies(skill.unit) ) {
		u.gainsHealth(ceil(u.stats.health*vars.value1/100));
		spawnFx();
	}
}


// RangeHeal (BeastMaster, FirstAid, PoisonFlask 참고)
function onSkill() {
    playAttack();
	for( t in skill.getTargets() ) {
        if( t.target.side == skill.unit.side ) {
            t.target.gainsHealth(ceil(t.target.stats.health*vars.value1/100));
            spawnFx();
        }
	}
}

// PoisonFlask
function onSkill() {
    playAttack();
    for( t in skill.getTargets() ) {
        if( skill.level == 2 && t.target.hasStatus(Status.Poison) ) {
            t.target.addStatus(Status.Vulnerability);
        }
        t.target.addStatus(Status.Poison, vars.value1);
    }
}

// FirstAid
function onSkill() {
    for( t in skill.getTargets() ) {
        t.target.gainsHealth( ceil(t.target.stats.health*vars.value1/100) , null);

        var cancelStatus = false;
        for( st in t.target.getAllStatus() ) {
            if( st.kind == Status.Poison || st.kind == Status.Bleeding || st.kind == Status.Burning ) {
                st.cancel();
                cancelStatus = true;
            }
        }
        t.target.gainsHealth( 0 , cancelStatus ? true : null);
    }
    play();
}


// ThroatyHowl (무리어미 스킬)
function onBeginRound() {
    if( !vars.done ) {
        vars.rats = getPlayerUnits().length + vars.value1;
        vars.done = true;
    }
}

function onSkill() {
    play();
    vars.rats += vars.value1;
}