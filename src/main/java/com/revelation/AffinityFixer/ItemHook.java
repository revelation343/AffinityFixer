package com.revelation.AffinityFixer;

import com.revelation.AffinityFixer.Action.AffinityFixerAction;
import com.revelation.AffinityFixer.Item.AffinityFixerItem;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.io.IOException;

public class ItemHook {

    public static AffinityFixerItem AFFINITY_FIXER = new AffinityFixerItem();

    public static void createAffinityFixer() throws IOException {
        AFFINITY_FIXER.createTemplate();
    }
    public static void registerAction(){
        ModActions.registerAction(new AffinityFixerAction());
    }
}
