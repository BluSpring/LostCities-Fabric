/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package mcjty.lostcities.forge.capabilities;

import io.github.fabricators_of_create.porting_lib.extensions.INBTSerializable;
import net.minecraft.nbt.Tag;

//Just a mix of the two, useful in patches to lower the size.
public interface ICapabilitySerializable<T extends Tag> extends ICapabilityProvider, INBTSerializable<T> {}
