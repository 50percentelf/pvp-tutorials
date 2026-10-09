# Pete Kayer Tutorial Capability Matrix

Research findings for each of Pete's official tutorials.
Populate entries through in-game testing before assigning `requiredBase` in `BuiltInTutorials`.

**Status legend**
- `confirmed` — value verified in-game
- `observed` — noted but not fully confirmed
- `unknown` — not yet tested

---

## How to use this document

For each custom tutorial being designed, identify which Pete tutorial supplies the
server-side mechanics you need (specials, freezes, prayer drain, food, etc.).
Only assign `requiredBase` in `BuiltInTutorials` once the relevant row here is confirmed.

---

## 1. Gear Switching (`GEAR_SWITCHING`)

**Region:** 10588  
**Identifying varbit:** 16309 (varpId 5889, value=1 on entry) — confirmed  
**NPC:** Pete Kayer (id 16577), Sparring partner (id 16609) — observed  
**Pete greeting:** "In this scenario, you'll master gear switching." — confirmed

| Capability | Status | Notes |
|---|---|---|
| Starting inventory | unknown | |
| Starting equipment | unknown | |
| Equipment freedom | unknown | Can you bring/use any gear? |
| Spec energy | unknown | Initial value, restoration rate |
| Spec energy restoration | unknown | Per tick / per kill / none |
| HP | unknown | Starting value, restoration |
| Prayer | unknown | Starting value, restoration |
| Food provided | unknown | Type and quantity |
| Runes provided | unknown | |
| Ammo provided | unknown | |
| Potions available | unknown | |
| Spell availability | unknown | |
| Freeze support | unknown | Can barrage be cast? |
| Smite available | unknown | |
| Protect Item available | unknown | |
| Attackable target | unknown | Player / NPC / both |
| Target HP / reset | unknown | Does the target reset HP? |
| Real damage occurs | unknown | Do hits register server-side? |
| Completion trigger | unknown | What ends the session? |
| Movement restrictions | unknown | Bounded arena? |
| Arena geometry | unknown | Size, obstacles |
| NPC behaviour | unknown | Does sparring partner attack back? |
| Exit behaviour | unknown | Teleported out after complete? |

**Priority questions for custom tutorials:**
- Can we use any equipped weapon or only the supplied ones?
- Do gear swaps register realistically (animation timing, attack speed)?

---

## 2. Special Attacks (`SPECIAL_ATTACKS`)

**Region:** 10588  
**Identifying varbit:** 16303 (varpId 5888, value=1 on entry) — confirmed

| Capability | Status | Notes |
|---|---|---|
| Starting spec energy | unknown | |
| Spec restoration rate | unknown | **HIGH PRIORITY** — needed for Bolt→Gmaul→AGS |
| Spec restoration trigger | unknown | Per tick / kill / unlimited? |
| Starting inventory | unknown | |
| Starting equipment | unknown | Are spec weapons provided? |
| HP | unknown | |
| Prayer | unknown | |
| Food / potions | unknown | |
| Attackable target | unknown | |
| Target HP / reset | unknown | |
| Real damage occurs | unknown | |
| Completion trigger | unknown | |
| Arena geometry | unknown | |

**Priority questions for custom tutorials:**
- Does spec energy restore per kill, per time, or is it unlimited?
- Are spec weapons supplied or must the player bring their own?
- Can multiple spec weapons be used sequentially in one session?

---

## 3. Prayer Protection (`PRAYER_PROTECTION`)

**Region:** 10588  
**Identifying varbit:** 10670 (varpId 1021, value=1 on entry) — confirmed

| Capability | Status | Notes |
|---|---|---|
| Prayer points | unknown | Starting value |
| Prayer drain rate | unknown | Is Smite active from NPC? |
| Prayer restoration | unknown | Restore potions, altar? |
| Smite availability (player) | unknown | Can the player use Smite? |
| Protect prayers available | unknown | |
| Protect Item available | unknown | |
| Attackable target | unknown | |
| HP / food | unknown | |
| Completion trigger | unknown | |

**Priority questions for custom tutorials:**
- Does the NPC use Smite, allowing prayer drain training?
- Can the player switch between Smite and protection prayers freely?

---

## 4. Power of Freezes (`POWER_OF_FREEZES`)

**Region:** 10588  
**Identifying varbit:** 16306 (varpId 5888, value=1 on entry) — confirmed

| Capability | Status | Notes |
|---|---|---|
| Runes provided | unknown | Ice barrage / blitz runes? |
| Freeze spell availability | unknown | |
| Freeze duration | unknown | Standard (16t barrage / 8t blitz)? |
| Freeze immune period | unknown | Standard 5t? |
| Target movement | unknown | Does NPC move freely? |
| Target freeze acceptance | unknown | Does NPC take the frozen state? |
| HP / food | unknown | |
| Prayer | unknown | |
| Arena geometry | unknown | Large enough for freeze → DD? |
| Completion trigger | unknown | |

**Priority questions for custom tutorials:**
- Do freeze spells behave identically to the main game (duration, immune timer)?
- Is the arena large enough for full movement/spacing drills?

---

## 5. Combo Eating (`COMBO_EATING`)

**Region:** 10588  
**Identifying varbit:** 16315 (varpId 5890, value=1 on entry) — confirmed

| Capability | Status | Notes |
|---|---|---|
| Food provided | unknown | Types (shark, karambwan?) |
| Food quantity | unknown | |
| Brew / restore potions | unknown | |
| HP depletion mechanism | unknown | NPC attacks, scripted drain? |
| HP restoration per food | unknown | Standard values? |
| Overeat prevention | unknown | Does the game block over-eating? |
| Eat timing window | unknown | Standard 3t eat delay? |
| Completion trigger | unknown | |

**Priority questions for custom tutorials:**
- Does the tutorial provide karambwan specifically?
- Is the 3-tick eat delay enforced (same as main game)?

---

## 6. Penultimate Challenge

**Region:** 11100  
**Identifying varbit:** none observed — requires chat-message detection  
**Distinguishing chat:** unknown — needs in-game testing

| Capability | Status | Notes |
|---|---|---|
| All capabilities | unknown | Region confirmed; content not yet tested |

---

## 7. Final Challenge

**Region:** 11100  
**Identifying varbit:** none observed — shares region with Penultimate Challenge  
**Distinguishing chat:** unknown — needs in-game testing

| Capability | Status | Notes |
|---|---|---|
| All capabilities | unknown | Region confirmed; content not yet tested |

---

## Research priority order

1. **Special Attacks spec restoration** — blocks all KO combo tutorials
2. **Gear Switching equipment freedom** — needed for Range→Staff and Mage→D'hide
3. **Power of Freezes freeze behaviour** — needed for all freeze/movement tutorials
4. **Combo Eating food types and timing** — needed for eating drills
5. **Prayer Protection prayer mechanics** — needed for smite tutorials
6. **Penultimate / Final Challenge distinction** — needed to route those arenas
