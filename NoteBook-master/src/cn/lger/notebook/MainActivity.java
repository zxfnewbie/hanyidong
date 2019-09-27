package cn.lger.notebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Cursor listItemCursor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 设置添加笔记按钮事件，切换activity
		this.findViewById(R.id.addNote).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent in = new Intent();
						in.setClassName(getApplicationContext(),
								"cn.lger.notebook.NoteEditActivity");
						startActivity(in);
					}
				});

		// 查询所有笔记，并将笔记展示出来
		listItemCursor = DBService.queryAll();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this,
				R.layout.note_item, listItemCursor, new String[] { "_id",
						"title", "createTime" }, new int[] { R.id.noteId,
						R.id.noteTitle, R.id.noteCreateTime },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		((ListView) this.findViewById(R.id.listNote)).setAdapter(adapter);

		initListNoteListener();

	}

	/**
	 * 初始化笔记列表的长按和点击事件
	 */
	private void initListNoteListener() {
		// 长按删除
		((ListView) this.findViewById(R.id.listNote))
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, final long id) {
						new AlertDialog.Builder(MainActivity.this)
								.setTitle("提示框")
								.setMessage("确认删除该笔记？？")
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface arg0,int arg1) {
												DBService.deleteNoteById((int) id);
												//删除后刷新列表
												MainActivity.this.onResume();
												Toast.makeText(
														MainActivity.this,
														"删除成功！！",
														Toast.LENGTH_LONG)
														.show();
											}
										}).setNegativeButton("取消", null).show();
						return true;
					}
				});

		//点击进行修改操作
		((ListView) this.findViewById(R.id.listNote))
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent in = new Intent();
						in.setClassName(view.getContext(),
								"cn.lger.notebook.NoteEditActivity");
						//将id数据放置到Intent
						in.putExtra("id", id);
						startActivity(in);
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 要求刷新主页列表笔记
		if (listItemCursor != null) {
			listItemCursor.requery();
		}
	}
}
