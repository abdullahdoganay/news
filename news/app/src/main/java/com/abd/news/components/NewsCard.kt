import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abd.news.model.Article
import com.abd.news.viewmodel.HomeScreenViewModel

@Composable
fun NewsCard(article: Article, viewModel: HomeScreenViewModel, onFavoriteClick: () -> Unit, onArticleClick: (Article) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onArticleClick(article) }
            .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(article.urlToImage) {
            imageBitmap.value = viewModel.getImage(article.urlToImage)
        }

        if (imageBitmap.value != null) {
            Image(
                bitmap = imageBitmap.value!!.asImageBitmap(),
                contentDescription = "com.abd.news.model.Article Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray)
            ) {
                Text(
                    text = "No Image",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            article.title?.let {
                Text(
                    text = it,
                    maxLines = 3,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            article.description?.let {
                Text(
                    text = it,
                    maxLines = 3,
                    style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(onClick = onFavoriteClick) {
            Text("★", color = Color.Yellow)
        }
    }
}
