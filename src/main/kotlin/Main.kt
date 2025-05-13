import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.sun.tools.javac.Main

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
    val scrollableState = rememberScrollState()
    val space = 6.dp
    val aNinja = MainViewModel.aFlow.collectAsState()
    val bNinja = MainViewModel.bFlow.collectAsState()
    val cNinja = MainViewModel.cFlow.collectAsState()
    val sNinja = MainViewModel.sFlow.collectAsState()
    val state = MainViewModel.state.collectAsState()
    val text = remember { mutableStateOf("") }
    MaterialTheme {
        Column {
            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.width(300.dp)) {
                    OutlinedTextField(
                        value = text.value,
                        label = { Text("请输入忍者名") },
                        onValueChange = { text.value = it },
                        modifier = Modifier.width(400.dp).padding(end = 20.dp).onPreviewKeyEvent {
                            if(it.key == Key.Enter){
                                MainViewModel.search(text.value)
                                true
                            } else {
                                false
                            }
                        },
                        maxLines = 1
                    )
                    Row(modifier = Modifier.align(Alignment.Center).padding(end = 30.dp)) {
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(imageVector = Icons.Default.Close, "", modifier = Modifier.onClick {
                            text.value = ""
                            MainViewModel.search("")
                        }.padding(top = 10.dp))
                    }

                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    MainViewModel.search(text.value)
                }, modifier = Modifier.height(55.dp)) {
                    Text("搜索")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        MainViewModel.save()
                    },
                    modifier = Modifier.height(55.dp),
                ) {
                    Text("保存")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        MainViewModel.reset()
                    },
                    modifier = Modifier.height(55.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White),
                ) {
                    Text("重置")
                }
                Spacer(modifier = Modifier.width(10.dp))
                RadioButton(selected = state.value == 0, onClick = { MainViewModel.showCur() })
                Text("可用忍者")

                RadioButton(selected = state.value == 1, onClick = { MainViewModel.showBan() })
                Text("禁用忍者")
            }
            Column(modifier = Modifier.verticalScroll(state = scrollableState, enabled = true).padding(10.dp)) {
                // sss
                Image(painter = painterResource("s.png"), contentDescription = null, modifier = Modifier.padding(3.dp))
                FlowRow(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(space),
                    verticalArrangement = Arrangement.spacedBy(space)
                ) {
                    sNinja.value.forEach {
                        NinjiaItem(it) { ninja: Ninja ->
                            if (state.value == 0) {
                                MainViewModel.ban(ninja)
                            } else {
                                MainViewModel.reborn(ninja)
                            }
                        }
                    }
                }


                // aaa
                Spacer(modifier = Modifier.height(20.dp))
                Image(painter = painterResource("a.png"), contentDescription = null, modifier = Modifier.padding(3.dp))
                FlowRow(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(space),
                    verticalArrangement = Arrangement.spacedBy(space)
                ) {
                    aNinja.value.forEach {
                        NinjiaItem(it) { ninja: Ninja ->
                            if (state.value == 0) {
                                MainViewModel.ban(ninja)
                            } else {
                                MainViewModel.reborn(ninja)
                            }
                        }
                    }
                }

                // bbb
                Spacer(modifier = Modifier.height(20.dp))
                Image(painter = painterResource("b.png"), contentDescription = null, modifier = Modifier.padding(3.dp))
                FlowRow(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(space),
                    verticalArrangement = Arrangement.spacedBy(space)
                ) {
                    bNinja.value.forEach {
                        NinjiaItem(it) { ninja: Ninja ->
                            if (state.value == 0) {
                                MainViewModel.ban(ninja)
                            } else {
                                MainViewModel.reborn(ninja)
                            }
                        }
                    }
                }

                // ccc
                Spacer(modifier = Modifier.height(20.dp))
                Image(painter = painterResource("c.png"), contentDescription = null, modifier = Modifier.padding(3.dp))
                FlowRow(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(space),
                    verticalArrangement = Arrangement.spacedBy(space)
                ) {
                    cNinja.value.forEach {
                        NinjiaItem(it) { ninja: Ninja ->
                            if (state.value == 0) {
                                MainViewModel.ban(ninja)
                            } else {
                                MainViewModel.reborn(ninja)
                            }
                        }
                    }
                }
                if(state.value == 0){
                    Text("完全免费，作者：B站/抖音：无情胡萝卜", fontSize = 8.sp)
                }
            }
        }
    }
}

fun main() = application {
//    val state = rememberWindowState(width = 1200.dp, height = 800.dp)
    Window(onCloseRequest = ::exitApplication, title = "火影手游BP") {
        App()
    }
}
