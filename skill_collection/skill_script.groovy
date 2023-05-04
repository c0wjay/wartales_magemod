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


// DopingShot
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

// 곰 광폭화: Ténacité sauvage, Berserk
// 베르나 투기장 무적: G2Arena1RuleLifeLinked, Arena_Willforce

// 도망침 디버프: Terror

//status column start
{
			"name": "status",
			"columns": [


대충 생각하고 있는건 한 직업 안에 세가지 트리 쑤셔넣는거고

힐러, 주술사, 법사 이렇게 세가지 트리일듯 (무리다 싶으면 힐러,주술사 두가지 트리 될수도)



번역작업은 export_zh.xml 참고


힐러

힐링 공식: (skill.unit.stats.willpower/50) * (vars.value1/100) * randInt(12,20)/16
전자는 캐릭터의 의지에 영향, 후자는 스킬 레벨에 따라 50, 100과 같이 주기.
거기에 랜덤 인자 곱 (75 ~ 125% 범위)

패시브 고민해야함.


주술사는 대충 좁은범위 (radius 1~2, distance 4) 광역 디버프 두가지 (공포, 슬로우나, 공/방 낮추는 커스텀 디버프 만들듯)
하나는 공포 스택 1~5 중 랜덤 적용, (레벨업 시, 일정 확률로 부동 적용.)
-> 의지 높을수록 높은 스택 적용될 확룔 높임.

다른 하나는 랜덤 디버프 적용
( 취약성 || 골절 || 실패)
레벨업시 일정 확률로 (감염 - 미친) 적용


아군 버프 생각중.
(Berserk || Elite || 실패)
레벨업시 일정 확률로 (Arena_Willforce) 적용

좀비화도 커스텀 버프인데 대충 베르나 투기장 버프 모티브로,

해당 유닛은 2턴간 피1 밑으로 안떨어지다

2턴 지나면 전투에서 배제되는 그런거 생각중


효과

디버프 - FogPowder 또는 ConfusingPowder


스킬 효율은 의지 스탯에 영향 받도록 수정.

즉 두가지 팩터에 영향을 받을텐데,

(skill.unit.stats.willpower/100) * (vars.value1/100) * randInt(12,20)/16


