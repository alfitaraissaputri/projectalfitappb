package com.example.projectalfitappb.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectalfitappb.antity.Todo
import com.example.projectalfitappb.databinding.ItemTodoBinding

class TodoAdapter(
    private val dataset: MutableList<Todo>,
    private val events: TodoItemEvents,
) : RecyclerView.Adapter<TodoAdapter.CustomViewHolder>() {
    interface TodoItemEvents {
        fun onDelete(todo: Todo)
    }

    inner class  CustomViewHolder(val view:ItemTodoBinding)
        :RecyclerView.ViewHolder(view.root){


            fun bindData(item: Todo ) {
                view.title.text = item.title
                view.description.text = item.description

                view.root.setOnClickListener {
                    events.onDelete(item)
                }

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return  CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val data = dataset[position]
        holder.bindData(data)
    }

    @SuppressLint("NotifyDataSetChanges")
    fun updateData(newData: List<Todo>) {
        dataset.clear()
        dataset.addAll(newData)
        notifyDataSetChanged()
    }
}

