import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

object MainViewModel {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val curC = mutableSetOf<Ninja>()
    private val banC = mutableSetOf<Ninja>()
    private val curB = mutableSetOf<Ninja>()
    private val banB = mutableSetOf<Ninja>()
    private val curA = mutableSetOf<Ninja>()
    private val banA = mutableSetOf<Ninja>()
    private val curS = mutableSetOf<Ninja>()
    private val banS = mutableSetOf<Ninja>()

    val cFlow = MutableStateFlow<MutableSet<Ninja>>(mutableSetOf())
    val bFlow = MutableStateFlow<MutableSet<Ninja>>(mutableSetOf())
    val aFlow = MutableStateFlow<MutableSet<Ninja>>(mutableSetOf())
    val sFlow = MutableStateFlow<MutableSet<Ninja>>(mutableSetOf())

    val state = MutableStateFlow(0) // 0 cur 1 ban

    init {
        reset()
    }

    fun search(name: String) {
        val searchA = mutableSetOf<Ninja>()
        val searchB = mutableSetOf<Ninja>()
        val searchC = mutableSetOf<Ninja>()
        val searchS = mutableSetOf<Ninja>()
        val fromA:MutableSet<Ninja>
        val fromB:MutableSet<Ninja>
        val fromC:MutableSet<Ninja>
        val fromS:MutableSet<Ninja>
        if(state.value == 0){
            fromA = curA.toMutableSet()
            fromB = curB.toMutableSet()
            fromC = curC.toMutableSet()
            fromS = curS.toMutableSet()
        } else {
            fromA = banA.toMutableSet()
            fromB = banB.toMutableSet()
            fromC = banC.toMutableSet()
            fromS = banS.toMutableSet()
        }
        for (ninja in fromA) {
            if (ninja.name.contains(name)) {
                searchA.add(ninja)
            }
        }
        for (ninja in fromB) {
            if (ninja.name.contains(name)) {
                searchB.add(ninja)
            }
        }
        for (ninja in fromC) {
            if (ninja.name.contains(name)) {
                searchC.add(ninja)
            }
        }

        for (ninja in fromS) {
            if (ninja.name.contains(name)) {
                searchS.add(ninja)
            }
        }
        scope.launch {
            aFlow.emit(searchA)
            bFlow.emit(searchB)
            cFlow.emit(searchC)
            sFlow.emit(searchS)
        }
    }

    fun showCur(){
        scope.launch {
            state.emit(0)
            cFlow.emit(curC.toMutableSet())
            bFlow.emit(curB.toMutableSet())
            aFlow.emit(curA.toMutableSet())
            sFlow.emit(curS.toMutableSet())
        }
    }

    fun showBan(){
        scope.launch {
            state.emit(1)
            cFlow.emit(banC.toMutableSet())
            bFlow.emit(banB.toMutableSet())
            aFlow.emit(banA.toMutableSet())
            sFlow.emit(banS.toMutableSet())
        }
    }

    fun ban(ninja: Ninja) {
        scope.launch {
            val cur: MutableSet<Ninja>
            val ban: MutableSet<Ninja>
            val flow:MutableStateFlow<MutableSet<Ninja>>
            when (ninja.level) {
                0 -> {
                    cur = curC
                    ban = banC
                    flow = cFlow
                }

                1 -> {
                    cur = curB
                    ban = banB
                    flow = bFlow
                }

                2 -> {
                    cur = curA
                    ban = banA
                    flow = aFlow
                }

                3 -> {
                    cur = curS
                    ban = banS
                    flow = sFlow
                }

                else -> {
                    cur = curC
                    ban = banC
                    flow = cFlow
                }
            }

            cur.remove(ninja)
            ban.add(ninja)

            flow.emit(mutableSetOf())
            flow.emit(cur.toMutableSet())
            showCur()
        }
    }

    fun reborn(ninja: Ninja) {
        scope.launch {
            val cur: MutableSet<Ninja>
            val ban: MutableSet<Ninja>
            val flow:MutableStateFlow<MutableSet<Ninja>>
            when (ninja.level) {
                0 -> {
                    cur = curC
                    ban = banC
                    flow = cFlow
                }

                1 -> {
                    cur = curB
                    ban = banB
                    flow = bFlow
                }

                2 -> {
                    cur = curA
                    ban = banA
                    flow = aFlow
                }

                3 -> {
                    cur = curS
                    ban = banS
                    flow = sFlow
                }

                else -> {
                    cur = curC
                    ban = banC
                    flow = cFlow
                }
            }

            cur.add(ninja)
            ban.remove(ninja)
            flow.emit(mutableSetOf())
            flow.emit(ban.toMutableSet())
            showBan()
        }
    }

    fun reset() {
        scope.launch {
            banA.clear()
            banB.clear()
            banC.clear()
            banS.clear()
            curA.clear()
            curB.clear()
            curC.clear()
            curS.clear()
            curA.addAll(aNinja)
            curS.addAll(sNinja)
            curB.addAll(bNinja)
            curC.addAll(cNinja)
            aFlow.emit(curA.toMutableSet())
            bFlow.emit(curB.toMutableSet())
            cFlow.emit(curC.toMutableSet())
            sFlow.emit(curS.toMutableSet())
            state.emit(0)
        }
    }

}