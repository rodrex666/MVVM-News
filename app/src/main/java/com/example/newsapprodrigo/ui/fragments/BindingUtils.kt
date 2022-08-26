package com.example.newsapprodrigo.ui.fragments

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.newsapprodrigo.models.Article
import com.example.newsapprodrigo.util.DateFormat

@BindingAdapter("TitleFormatted")
fun TextView.setTitleFormatted(item: Article?){
    item?.let {
        text = item.title
    }
}
@BindingAdapter("DescriptionView")
fun TextView.setDescriptionView(item: Article?){
    item?.let {
        text = item.description
    }
}
@BindingAdapter("PublishedView")
fun TextView.setPublishedView(item: Article?){
    item?.let {
        text = DateFormat.changeDateFormat(item.publishedAt)
    }
}
@BindingAdapter("SourceView")
fun TextView.setSourceView(item: Article?){
    item?.let {
        text = item.source?.name
    }
}
@BindingAdapter("ArticleImageView")
fun ImageView.setArticleImageView(item: Article){
    Glide.with(context).load(item.urlToImage).into(this)
}
