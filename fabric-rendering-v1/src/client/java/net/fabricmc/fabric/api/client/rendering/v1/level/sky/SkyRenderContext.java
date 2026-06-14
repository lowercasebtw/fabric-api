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

package net.fabricmc.fabric.api.client.rendering.v1.level.sky;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;

import net.fabricmc.fabric.api.client.rendering.v1.level.AbstractLevelRenderContext;

/**
 * Context passed to sky rendering events.
 *
 * <p>This context is scoped to the level render frame and exposes the sky-specific render state extracted for that
 * frame. Use {@link net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents#END_EXTRACTION} to prepare
 * additional render state before sky rendering begins.
 */
@ApiStatus.NonExtendable
public interface SkyRenderContext extends AbstractLevelRenderContext {
	/**
	 * The sky renderer instance used by the current sky pass.
	 *
	 * @return sky renderer instance
	 */
	SkyRenderer skyRenderer();

	/**
	 * The sky render state for the current frame.
	 *
	 * @return sky render state
	 */
	SkyRenderState skyRenderState();

	/**
	 * The camera render state for the current frame.
	 *
	 * @return camera render state
	 */
	CameraRenderState cameraRenderState();
}
