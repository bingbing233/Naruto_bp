import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File

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
        read2()
    }

    fun search(name: String) {
        val searchA = mutableSetOf<Ninja>()
        val searchB = mutableSetOf<Ninja>()
        val searchC = mutableSetOf<Ninja>()
        val searchS = mutableSetOf<Ninja>()
        val fromA: MutableSet<Ninja>
        val fromB: MutableSet<Ninja>
        val fromC: MutableSet<Ninja>
        val fromS: MutableSet<Ninja>
        if (state.value == 0) {
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

    fun showCur() {
        scope.launch {
            state.emit(0)
            cFlow.emit(curC.toMutableSet())
            bFlow.emit(curB.toMutableSet())
            aFlow.emit(curA.toMutableSet())
            sFlow.emit(curS.toMutableSet())
        }
    }

    fun showBan() {
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
            val flow: MutableStateFlow<MutableSet<Ninja>>
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
            val flow: MutableStateFlow<MutableSet<Ninja>>
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

    fun save() {
        kotlin.runCatching {
            val gson = Gson()
            val json = gson.toJson(curA)

            println("json:$json")
            val fCurS = File("curS.txt")
            val fCurA = File("curA.txt")
            val fCurB = File("curB.txt")
            val fCurC = File("curC.txt")

            val fBanS = File("banS.txt")
            val fBanA = File("banA.txt")
            val fBanB = File("banB.txt")
            val fBanC = File("banC.txt")

            if (!fCurA.exists()) {
                fCurA.createNewFile()
            }
            if (!fCurB.exists()) {
                fCurB.createNewFile()
            }
            if (!fCurC.exists()) {
                fCurC.createNewFile()
            }
            if (!fCurS.exists()) {
                fCurS.createNewFile()
            }
            if (!fBanA.exists()) {
                fBanA.createNewFile()
            }
            if (!fBanB.exists()) {
                fBanB.createNewFile()
            }
            if (!fBanC.exists()) {
                fBanC.createNewFile()
            }
            if (!fBanS.exists()) {
                fBanS.createNewFile()
            }

            fCurA.writeText(gson.toJson(curA))
            fCurB.writeText(gson.toJson(curB))
            fCurC.writeText(gson.toJson(curC))
            fCurS.writeText(gson.toJson(curS))

            fBanA.writeText(gson.toJson(banA))
            fBanB.writeText(gson.toJson(banB))
            fBanC.writeText(gson.toJson(banC))
            fBanS.writeText(gson.toJson(banS))

        }.onFailure {
            println(it)
        }
    }

    fun save2() {
        scope.launch {
            saveData("curA", curA)
            saveData("curB", curB)
            saveData("curC", curC)
            saveData("curS", curS)
            saveData("banA", banA)
            saveData("banB", banB)
            saveData("banC", banC)
            saveData("banS", banS)
        }
    }

    private fun read2() {
        scope.launch {
            val strCurA = readData("curA")
            val strCurB = readData("curB")
            val strCurC = readData("curC")
            val strCurS = readData("curS")
            val strBanA = readData("banA")
            val strBanB = readData("banB")
            val strBanC = readData("banC")
            val strBanS = readData("banS")
            if (strCurA.isEmpty()) {
                curA.addAll(aNinja)
            } else {
                curA.addAll(json2Set(strCurA))
            }
            if (strCurB.isEmpty()) {
                curB.addAll(bNinja)
            } else {
                curB.addAll(json2Set(strCurB))
            }
            if (strCurC.isEmpty()) {
                curC.addAll(cNinja)
            } else {
                curC.addAll(json2Set(strCurC))
            }
            if (strCurS.isEmpty()) {
                curS.addAll(sNinja)
            } else {
                curS.addAll(json2Set(strCurS))
            }

            if (strBanA.isNotEmpty()) {
                banA.addAll(json2Set(strBanA))
            }

            if (strBanB.isNotEmpty()) {
                banB.addAll(json2Set(strBanB))
            }

            if (strBanC.isNotEmpty()) {
                banC.addAll(json2Set(strBanC))
            }

            if (strBanS.isNotEmpty()) {
                banS.addAll(json2Set(strBanS))
            }

            aFlow.emit(curA.toMutableSet())
            bFlow.emit(curB.toMutableSet())
            cFlow.emit(curC.toMutableSet())
            sFlow.emit(curS.toMutableSet())
        }
    }

    private fun json2Set(json: String): MutableSet<Ninja> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Ninja>>() {}
        return gson.fromJson(json, type).toMutableSet()
    }

    private suspend fun saveData(key: String, data: Set<Ninja>) {
        val gson = Gson()
        dataStores.edit {
            it[stringPreferencesKey(key)] = gson.toJson(data)
        }
    }

    private suspend fun readData(key: String): String {
        return dataStores.data.map {
            it[stringPreferencesKey(key)] ?: ""
        }.first()
    }

    private fun initDefaultData() {
        scope.launch {
            curA.addAll(aNinja)
            curB.addAll(bNinja)
            curC.addAll(cNinja)
            curS.addAll(sNinja)
            aFlow.emit(curA.toMutableSet())
            bFlow.emit(curB.toMutableSet())
            cFlow.emit(curC.toMutableSet())
            sFlow.emit(curS.toMutableSet())
        }
    }

    private fun readLocal() {
        scope.launch {
            kotlin.runCatching {
                val type = object : TypeToken<ArrayList<Ninja>>() {}
                val fCurS = File("curS.txt")
                val fCurA = File("curA.txt")
                val fCurB = File("curB.txt")
                val fCurC = File("curC.txt")

                val fBanS = File("banS.txt")
                val fBanA = File("banA.txt")
                val fBanB = File("banB.txt")
                val fBanC = File("banC.txt")
                val gson = Gson()
                if (!fCurS.exists()) {
                    fCurS.createNewFile()
                }
                if (!fCurA.exists()) {
                    fCurA.createNewFile()
                }
                if (!fCurB.exists()) {
                    fCurB.createNewFile()
                }
                if (!fCurC.exists()) {
                    fCurC.createNewFile()
                }

                if (!fBanS.exists()) {
                    fBanS.createNewFile()
                }

                if (!fBanA.exists()) {
                    fBanA.createNewFile()
                }

                if (!fBanB.exists()) {
                    fBanB.createNewFile()
                }

                if (!fBanC.exists()) {
                    fBanC.createNewFile()
                }

                curA.addAll(gson.fromJson(fCurA.readText(), type).toMutableSet())
                curB.addAll(gson.fromJson(fCurB.readText(), type).toMutableSet())
                curC.addAll(gson.fromJson(fCurC.readText(), type).toMutableSet())
                curS.addAll(gson.fromJson(fCurS.readText(), type).toMutableSet())

                banA.addAll(gson.fromJson(fBanA.readText(), type).toMutableSet())
                banB.addAll(gson.fromJson(fBanB.readText(), type).toMutableSet())
                banC.addAll(gson.fromJson(fBanC.readText(), type).toMutableSet())
                banS.addAll(gson.fromJson(fBanS.readText(), type).toMutableSet())

                if (curA.isEmpty() || curB.isEmpty() || curC.isEmpty() || curS.isEmpty()) {
                    initDefaultData()
                }

                aFlow.emit(curA.toMutableSet())
                bFlow.emit(curB.toMutableSet())
                cFlow.emit(curC.toMutableSet())
                sFlow.emit(curS.toMutableSet())
            }.onFailure {
                initDefaultData()
                println(it)
            }
        }

    }

}