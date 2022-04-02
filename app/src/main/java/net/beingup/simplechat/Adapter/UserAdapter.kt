package net.beingup.simplechat.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import net.beingup.simplechat.Activities.Messenger
import net.beingup.simplechat.Activities.StartPage
import net.beingup.simplechat.Adapter.UserAdapter.ViewHolder
import net.beingup.simplechat.R
import net.beingup.simplechat.pojo.MessagePojo
import net.beingup.simplechat.pojo.SingleUserInfo

class UserAdapter(private val mContext: Context, private val mUsers: List<SingleUserInfo>, private val isChat: Boolean, private val showUnseen: Boolean, private val myId: String, private val message: List<MessagePojo>):
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * In this function system decorate chat list with last message, online status, seen or unseen message and profile image
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUsers[position]
        holder.username.text = user.username

        if (user.profile_image == mContext.getString(R.string.f_default))
            holder.profileImage.setImageResource(R.drawable.jack)
        else Picasso.get().load(user.profile_image).into(holder.profileImage)

        if (isChat and (user.status == mContext.getString(R.string.f_on))) holder.statusRing.visibility = View.VISIBLE

        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, Messenger::class.java)
            intent.putExtra("target_user", user.id)
            mContext.startActivity(intent)
        }

        //show last message and unseen point
        if (showUnseen){
            val lastMsg = message[position]
            var text =  lastMsg.message

            holder.lastMessage.visibility = View.VISIBLE
            if (text == mContext.getString(R.string.f_null)){
                holder.lastMessage.text = "Start first message."
                holder.lastMessage.setTextColor(ResourcesCompat.getColor(mContext.resources, R.color.gray, null))
            }
            else{
                if (lastMsg.sender == myId){
                    holder.lastMessage.text = text
                    holder.lastMessage.setTextColor(ResourcesCompat.getColor(mContext.resources, R.color.gray, null))
                }
                else{
                    if (lastMsg.isSeen == "true"){
                        holder.lastMessage.text = text
                        holder.lastMessage.setTextColor(ResourcesCompat.getColor(mContext.resources, R.color.main, null))
                    }
                    else{
                        holder.unseenPoint.visibility = View.VISIBLE
                        holder.lastMessage.text = text
                        holder.anUserItem.setBackgroundResource(R.color.unseen_bg)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    /**
     * view holder class for cast views.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById(R.id.user_name) as TextView
        val profileImage = itemView.findViewById(R.id.user_image) as ImageView
        val statusRing = itemView.findViewById(R.id.online) as TextView
        val lastMessage = itemView.findViewById(R.id.last_message) as TextView
        val unseenPoint = itemView.findViewById(R.id.unseen) as TextView
        val anUserItem = itemView.findViewById(R.id.an_user_item) as RelativeLayout
    }
}
