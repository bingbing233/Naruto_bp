import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun NinjiaItem(ninja: Ninja,onClick:(Ninja)->Unit = {}) {
    val size = 80.dp
    Card (modifier = Modifier.clickable { onClick.invoke(ninja) }.padding(2.dp)){
        Column(modifier = Modifier.padding(2.dp)) {
            Image(painter = painterResource(ninja.imagePath), ninja.name, modifier = Modifier.width(size).height(size))
            Text(text = ninja.name, fontSize = 14.sp, modifier = Modifier.width(size).align(Alignment.CenterHorizontally).basicMarquee(), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}