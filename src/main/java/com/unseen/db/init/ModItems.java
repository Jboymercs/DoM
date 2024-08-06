package com.unseen.db.init;

import com.unseen.db.items.ItemBase;
import com.unseen.db.items.ItemOrb;
import com.unseen.db.items.ItemStaff;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static final List<Item> ITEMS = new ArrayList<Item>();
    public static final Item INVISIBLE = new ItemBase("invisible", null);

    public static final Item ORB_PROJECTILE = new ItemOrb("orb", null);
    public static final Item HIEROPHANT_STAFF = new ItemStaff("staff" , CreativeTabs.COMBAT, "staff_desc");
}
