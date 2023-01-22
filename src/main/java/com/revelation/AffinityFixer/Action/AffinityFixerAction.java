package com.revelation.AffinityFixer.Action;

import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.Affinities;
import com.wurmonline.server.skills.Affinity;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.skills.SkillSystem;
import com.revelation.AffinityFixer.Item.AffinityFixerItem;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AffinityFixerAction implements ModAction {
    private static Logger logger = Logger.getLogger(AffinityFixerAction.class.getName());

    private final short actionId;
    private final ActionEntry actionEntry;

    public AffinityFixerAction() {
        logger.log(Level.WARNING, "AffinityFixerAction()");

        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Fix affinity",
                "fixing",
                new int[] { 0 /* ACTION_TYPE_NOMOVE */ }	// 6 /* ACTION_TYPE_NOMOVE */, 48 /* ACTION_TYPE_ENEMY_ALWAYS */, 36 /* ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM */
        );
        ModActions.registerAction(actionEntry);
    }

    public static boolean hasAffinityFixer(Creature performer){
        //logger.info("Checking if creature has affinity fixer.");
        for(Item i : performer.getInventory().getItems()){
            if(i.getTemplateId() == AffinityFixerItem.templateId){
                //logger.info("Has affinity fixer.");
                return true;
            }
        }
        //logger.info("No affinity fixer found.");
        return false;
    }

    @Override
    public BehaviourProvider getBehaviourProvider()
    {
        return new BehaviourProvider() {
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Skill skill) {
                if(performer instanceof Player && skill.affinity > 0 && hasAffinityFixer(performer)) {
                    return Collections.singletonList(actionEntry);
                }

                return null;
            }

            // Never called
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Skill skill) {
                if(performer instanceof Player && source != null && source.getTemplateId() == AffinityFixerItem.templateId && source.getData() == 0) {
                    return Collections.singletonList(actionEntry);
                }

                return null;
            }
        };
    }

    @Override
    public ActionPerformer getActionPerformer()
    {
        return new ActionPerformer() {

            @Override
            public short getActionId() {
                return actionId;
            }
            @Override
            public boolean action(Action act, Creature performer, Skill skill, short action, float counter) {
                performer.getCommunicator().sendNormalServerMessage("Activate your affinity fixer to fix the affinity.");
                return true;
            }

            // Without activated object
            @Override
            public boolean action(Action act, Creature performer, Item source, Skill skill, short action, float counter) {
                if(performer instanceof Player){
                    Player player = (Player) performer;
                    if (source.getTemplate().getTemplateId() != AffinityFixerItem.templateId){
                        player.getCommunicator().sendSafeServerMessage("You must use an Affinity Fixer to fix an affinity.");
                        return true;
                    }
                    int skillNum = skill.getNumber();
                    int[] validAffinitySkills = {1028,1029,1023,10068,10075,10076,10077,10084,10093,10094};
                    boolean validAffinity = false;
                    for (int validSkill : validAffinitySkills) {
                        if (validSkill == skillNum) {
                            validAffinity = true;
                            break;
                        }
                    }
                    if (!validAffinity) {
                        player.getCommunicator().sendSafeServerMessage("This skill is not considered a PVP skill and cannot be fixed.");
                        return true;
                    }
                    if(source.getData() > 0){
                        player.getCommunicator().sendSafeServerMessage("The affinity already has already been fixed.");
                        return true;
                    }
                    Affinity[] affs = Affinities.getAffinities(player.getWurmId());
                    int affinityCount = 0;
                    for (Affinity affinity : affs) {
                        if(affinity.getNumber() > 0){
                            logger.info("Adding "+affinity.getNumber()+" affinities to total due to skill "+SkillSystem.getNameFor(affinity.getSkillNumber()));
                            affinityCount += affinity.getNumber();
                        }
                    }
                    if(affinityCount > 1){
                        logger.info("Player "+performer.getName()+" has too many affinities to utilize an affinity fixer.");
                        performer.getCommunicator().sendNormalServerMessage("You must have only 1 affinity in total before using an "+source.getName()+".");
                        return true;
                    }
                    for (Affinity affinity : affs) {
                        if (affinity.getSkillNumber() != skillNum) continue;
                        if (affinity.getNumber() < 1){
                            break;
                        }
                        Affinities.setAffinity(player.getWurmId(), skillNum, affinity.getNumber() - 1, false);
                        logger.info("Setting "+source.getName()+" data to "+skillNum);
                        source.setData(skill.getNumber());
                        source.setName("captured "+skill.getName()+" affinity");
                        source.setQualityLevel(0);
                        performer.getCommunicator().sendSafeServerMessage("Your "+skill.getName()+" affinity is removed!!");

                        int[] affinitySkills = {1,2,3,100,101,102,103,104,105,106,1005,1007,1008,1009,1010,1011,1013,1014,1020,1021,1022,1024,1025,1026,1027,1030,1031,1032,1033,10069,10001,10002,10003,10004,10005,10006,10007,10008,10009,10010,10011,10012,10013,10014,10015,10016,10017,10018,10019,10020,10021,10022,10023,10024,10025,10026,10027,10028,10029,10030,10031,10032,10033,10034,10035,10036,10040,10041,10042,10043,10044,10045,10046,10047,10048,10049,10050,10051,10052,10053,10054,10055,10056,10057,10058,10059,10060,10061,10062,10063,10064,10065,10066,10067,10070,10071,10072,10073,10074,10078,10079,10080,10081,10082,10085,10086,10087,10088,10089,10090,10091,10092,10095};
                        int randomAffinityIndex = Server.rand.nextInt(affinitySkills.length);
                        int randomAffinitySkillNum = affinitySkills[randomAffinityIndex];
                        Affinities.setAffinity(player.getWurmId(), randomAffinitySkillNum, 1, false);
                        performer.getCommunicator().sendSafeServerMessage("You got a new random affinity: " + SkillSystem.getNameFor(randomAffinitySkillNum));
                        return true;
                    }
                    // Only called if the affinity is not found or it breaks from having less than one.
                    player.getCommunicator().sendNormalServerMessage("You must have an affinity in the skill to fix it.");
                }else{
                    logger.info("Somehow a non-player activated an Affinity Fixer...");
                }
                return true;
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter)
            {
                return this.action(act, performer, target, action, counter);
            }


        }; // ActionPerformer
    }
}