/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.client.rendering.level.sky;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.client.rendering.v1.level.sky.SkyRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.sky.SkyRenderEvents;

public class SkyRenderEventsTest {
	@Test
	void preSkyCallsAllListenersAndAggregatesCancellation() {
		List<String> calls = new ArrayList<>();
		ListenerState state = new ListenerState();
		SkyRenderContext context = mock(SkyRenderContext.class);

		SkyRenderEvents.PRE_SKY.register(skyContext -> record(state, calls, "first", true));
		SkyRenderEvents.PRE_SKY.register(skyContext -> record(state, calls, "second", false));
		SkyRenderEvents.PRE_SKY.register(skyContext -> record(state, calls, "third", false));

		try {
			assertTrue(SkyRenderEvents.PRE_SKY.invoker().execute(context));
			assertEquals(List.of("first", "second", "third"), calls);
		} finally {
			state.active = false;
		}
	}

	@Test
	void preCustomElementRespectsPhaseOrderAndAggregatesCancellation() {
		List<String> calls = new ArrayList<>();
		ListenerState state = new ListenerState();

		SkyRenderEvents.PRE_CUSTOM_ELEMENT.register(SkyRenderEvents.LATE_PHASE, (key, context) -> record(state, calls, "late", true));
		SkyRenderEvents.PRE_CUSTOM_ELEMENT.register((key, context) -> record(state, calls, "default", false));
		SkyRenderEvents.PRE_CUSTOM_ELEMENT.register(SkyRenderEvents.EARLY_PHASE, (key, context) -> record(state, calls, "early", false));

		try {
			boolean cancelled = SkyRenderEvents.PRE_CUSTOM_ELEMENT.invoker().execute(testIdentifier("custom_element"), new Object());

			assertTrue(cancelled);
			assertEquals(List.of("early", "default", "late"), calls);
		} finally {
			state.active = false;
		}
	}

	private static boolean record(ListenerState state, List<String> calls, String call, boolean cancelled) {
		if (!state.active) {
			return false;
		}

		calls.add(call);
		return cancelled;
	}

	private static Identifier testIdentifier(String path) {
		return Identifier.fromNamespaceAndPath("fabric-rendering-v1-test", path);
	}

	// Events do not support unregistering listeners, so tests disable their callbacks after assertions.
	private static final class ListenerState {
		private boolean active = true;
	}
}
