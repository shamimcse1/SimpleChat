package net.beingup.simplechat.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import net.beingup.simplechat.R
import net.beingup.simplechat.pojo.MessagePojo



class MessageAdapter (private val mContext: Context, private val mChat: List<MessagePojo>, private var img_url: String, private var myId: String):
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    // This two variable store data for decorate message
    private var pastSender = "0"
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Define message layout
        return if (mChat[viewType].sender == myId){
            val view = LayoutInflater.from(mContext).inflate(R.layout.chat_right, parent, false)
            ViewHolder(view)
        } else{
            val view = LayoutInflater.from(mContext).inflate(R.layout.chat_left, parent, false)
            ViewHolder(view)
        }
    }

    /**
     * Here all message decorate with scrolling
     */
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val chat = mChat[position]
        val currentSender = chat.sender

        // set text message
        holder.message.text = chat.message

        // set text message background
        if ((position < lastPosition) or (lastPosition == -1)){ // new message or scroll to top [index grow down]
            if (position>0){
                val futureChat = mChat[position-1]
                val futureSender = futureChat.sender

                if (position == mChat.size-1){
                    if (futureSender != currentSender){
                        holder.topMargin.visibility = View.VISIBLE

                        if (currentSender == myId)
                            holder.message.setBackgroundResource(R.drawable.background_right0)
                        else
                            holder.message.setBackgroundResource(R.drawable.background_left0)
                    }
                    else{
                        if (currentSender == myId)
                            holder.message.setBackgroundResource(R.drawable.background_right1)
                        else
                            holder.message.setBackgroundResource(R.drawable.background_left1)
                    }
                }
                else{
                    when{
                        (pastSender == currentSender) and (futureSender == currentSender) ->{
                            if (currentSender == myId)
                                holder.message.setBackgroundResource(R.drawable.background_right2)
                            else
                                holder.message.setBackgroundResource(R.drawable.background_left2)
                        }
                        (pastSender == currentSender) and (futureSender != currentSender) ->{
                            holder.topMargin.visibility = View.VISIBLE

                            if (currentSender == myId)
                                holder.message.setBackgroundResource(R.drawable.background_right3)
                            else
                                holder.message.setBackgroundResource(R.drawable.background_left3)
                        }
                        (pastSender != currentSender) and (futureSender == currentSender) ->{
                            if (currentSender == myId)
                                holder.message.setBackgroundResource(R.drawable.background_right1)
                            else
                                holder.message.setBackgroundResource(R.drawable.background_left1)
                        }
                        (pastSender != currentSender) and (futureSender != currentSender) ->{
                            holder.topMargin.visibility = View.VISIBLE

                            if (currentSender == myId)
                                holder.message.setBackgroundResource(R.drawable.background_right0)
                            else
                                holder.message.setBackgroundResource(R.drawable.background_left0)
                        }
                    }
                }
            }
            else{
                holder.topMargin.visibility = View.VISIBLE

                if (pastSender == chat.sender){
                    if (chat.sender == myId)
                        holder.message.setBackgroundResource(R.drawable.background_right3)
                    else
                        holder.message.setBackgroundResource(R.drawable.background_left3)
                }
                else{
                    if (chat.sender == myId)
                        holder.message.setBackgroundResource(R.drawable.background_right0)
                    else
                        holder.message.setBackgroundResource(R.drawable.background_left0)
                }
            }
        }
        else{ // scroll bellow from top [index grow up]
            if (position < (mChat.size-1)){
                val futureChat = mChat[position+1]
                val futureSender = futureChat.sender

                when{
                    (pastSender == currentSender) and (futureSender == currentSender) ->{
                        if (currentSender == myId)
                            holder.message.setBackgroundResource(R.drawable.background_right2)
                        else
                            holder.message.setBackgroundResource(R.drawable.background_left2)
                    }
                    (pastSender == currentSender) and (futureSender != currentSender) ->{
                        //holder.topMargin.visibility = View.VISIBLE

                        if (currentSender == myId)
                            holder.message.setBackgroundResource(R.drawable.background_right1)
                        else
                            holder.message.setBackgroundResource(R.drawable.background_left1)
                    }
                    (pastSender != currentSender) and (futureSender == currentSender) ->{
                        holder.topMargin.visibility = View.VISIBLE
                        if (currentSender == myId)
                            holder.message.setBackgroundResource(R.drawable.background_right3)
                        else
                            holder.message.setBackgroundResource(R.drawable.background_left3)
                    }
                    (pastSender != currentSender) and (futureSender != currentSender) ->{
                        holder.topMargin.visibility = View.VISIBLE

                        if (currentSender == myId)
                            holder.message.setBackgroundResource(R.drawable.background_right0)
                        else
                            holder.message.setBackgroundResource(R.drawable.background_left0)
                    }
                }
            }
            else{ // last message
                if (pastSender == chat.sender){
                    if (chat.sender == myId)
                        holder.message.setBackgroundResource(R.drawable.background_right1)
                    else
                        holder.message.setBackgroundResource(R.drawable.background_left1)
                }
                else{
                    holder.topMargin.visibility = View.VISIBLE
                    if (chat.sender == myId)
                        holder.message.setBackgroundResource(R.drawable.background_right0)
                    else
                        holder.message.setBackgroundResource(R.drawable.background_left0)
                }
            }
        }

        // process text message
        if (chat.sender == myId){
            if (chat.isSeen == "true"){
                if (position == mChat.size-1){
                    holder.seen.visibility = View.VISIBLE
                    if (img_url == mContext.getString(R.string.f_default))
                        holder.seen.setImageResource(R.drawable.jack)
                    else {
                        Picasso.get().load(img_url).into(holder.seen)
                    }
                }
                else holder.margin.visibility = View.VISIBLE
            }
            else{
                holder.delivary.visibility = View.VISIBLE
                holder.delivary.setImageResource(R.drawable.delivaried)
            }

            pastSender = myId // this is for left site user image
        }
        else{ // ‍set profile image
            if (position < lastPosition){
                if (pastSender == chat.sender){
                    holder.profile_image.visibility = View.GONE
                    holder.margin.visibility = View.VISIBLE
                }
                else{
                    if (img_url == mContext.getString(R.string.f_default))
                        holder.profile_image.setImageResource(R.drawable.jack)
                    else Picasso.get().load(img_url).into(holder.profile_image)
                }

            }
            else{
                if (position < (mChat.size-1)){
                    val futureChat = mChat[position+1]
                    val futureSender = futureChat.sender
                    if (futureSender == chat.sender){
                        holder.profile_image.visibility = View.GONE
                        holder.margin.visibility = View.VISIBLE
                    }
                    else{
                        if (img_url == mContext.getString(R.string.f_default))
                            holder.profile_image.setImageResource(R.drawable.jack)
                        else Picasso.get().load(img_url).into(holder.profile_image)
                    }
                }
            }

            pastSender = chat.sender
        }

        // store last position
        lastPosition = position
    }

    override fun getItemCount(): Int {
        return mChat.size
    }

    /**
     * view holder class for cast views.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message = itemView.findViewById(R.id.text_message) as TextView
        val profile_image = itemView.findViewById(R.id.chat_profile) as ImageView
        val delivary = itemView.findViewById(R.id.delivary) as ImageView
        val seen = itemView.findViewById(R.id.seen) as ImageView
        val margin = itemView.findViewById(R.id.marginLeftRight) as TextView
        val topMargin = itemView.findViewById(R.id.top_margin) as TextView
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
