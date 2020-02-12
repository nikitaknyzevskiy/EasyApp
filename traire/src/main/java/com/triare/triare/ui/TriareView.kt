/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare.ui

import com.triare.triare.ui.actions.OnError
import com.triare.triare.ui.actions.OnLoading

interface TriareView : OnLoading,
    OnError