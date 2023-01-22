package com.revelation.AffinityFixer.Item;

import com.revelation.AffinityFixer.AffinityFixer;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.items.Materials;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class AffinityFixerItem implements ItemTypes, MiscConstants {
    public static Logger logger = Logger.getLogger(AffinityFixerItem.class.getName());
    public static int templateId;

    public void createTemplate() throws IOException{
        String name = "Affinity Corrector";
        ItemTemplateBuilder itemBuilder = new ItemTemplateBuilder("mod.item.affinity.corrector");
        itemBuilder.name(name, "Affinity Fixers", "An orb that can remove PVP affinities and provide a new random affinity.");
        itemBuilder.itemTypes(new short[]{
                ItemTypes.ITEM_TYPE_MAGIC,
                ItemTypes.ITEM_TYPE_FULLPRICE,
                ItemTypes.ITEM_TYPE_HASDATA,
                ItemTypes.ITEM_TYPE_NOSELLBACK,
                ItemTypes.ITEM_TYPE_ALWAYS_BANKABLE
        });
        itemBuilder.imageNumber((short) 919);
        itemBuilder.behaviourType((short) 1);
        itemBuilder.combatDamage(0);
        itemBuilder.decayTime(Long.MAX_VALUE);
        itemBuilder.dimensions(1, 1, 1);
        itemBuilder.primarySkill((int) NOID);
        itemBuilder.bodySpaces(EMPTY_BYTE_PRIMITIVE_ARRAY);
        itemBuilder.modelName("model.artifact.orbdoom");
        itemBuilder.difficulty(5.0f);
        itemBuilder.weightGrams(500);
        itemBuilder.material(Materials.MATERIAL_CRYSTAL);
        itemBuilder.value(AffinityFixer.Value);
        itemBuilder.isTraded(true);

        ItemTemplate template = itemBuilder.build();
        templateId = template.getTemplateId();
        logger.info(name+" TemplateID: "+templateId);
    }
}
