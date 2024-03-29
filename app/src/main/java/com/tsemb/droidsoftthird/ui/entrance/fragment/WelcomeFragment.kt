/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tsemb.droidsoftthird.ui.entrance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tsemb.droidsoftthird.ui.entrance.Screen
import com.tsemb.droidsoftthird.vm.entrance.WelcomeViewModel
import com.tsemb.droidsoftthird.composable.entrance.WelcomeEvent
import com.tsemb.droidsoftthird.composable.entrance.WelcomeScreen
import com.tsemb.droidsoftthird.ui.entrance.navigate
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment: Fragment() {

    private val viewModel: WelcomeViewModel by viewModels ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->
                navigate(navigateTo, Screen.Welcome)
            }//レイアウトを設定する作業みたい。
        }

        if (FirebaseAuth.getInstance().currentUser != null) viewModel.alreadySignedIn()

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    WelcomeScreen (
                        onEvent = { event ->
                            when (event) {
                                is WelcomeEvent.SignIn -> viewModel.signIn()
                                is WelcomeEvent.SignUp -> viewModel.signUp()
                            }
                        }
                    )
                }
            }
        }
    }
}
