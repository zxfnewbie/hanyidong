package cn.lger.notebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class NoteEditActivity extends Activity {

	private EditText titleEditText = null;
	private EditText contentEditText = null;
	private String noteId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_editor);

		titleEditText = (EditText) NoteEditActivity.this
				.findViewById(R.id.title);
		contentEditText = (EditText) NoteEditActivity.this
				.findViewById(R.id.content);

		initNoteEditValue();
		
		//ȡ����ť����
		this.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						NoteEditActivity.this.finish();
					}
				});

		this.findViewById(R.id.save).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				final String title = titleEditText.getText().toString();
				final String content = contentEditText.getText().toString();
				
				//�жϱ���������Ƿ�Ϊ�գ���Ϊ�ղ��ܱ���
				if ("".equals(title) || "".equals(content)) {
					Toast.makeText(NoteEditActivity.this, "����������ݲ���Ϊ��",
							Toast.LENGTH_LONG).show();
					return;
				}
				
				//��ʾ����
				new AlertDialog.Builder(NoteEditActivity.this)
						.setTitle("��ʾ��")
						.setMessage("ȷ������ʼ��𣿣�")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										ContentValues values = new ContentValues();
										values.put("title", title);
										values.put("content", content);
										
										//���noteId��Ϊ����ô���Ǹ��²�����Ϊ�վ�����Ӳ���
										if (null == noteId || "".equals(noteId))
											DBService.addNote(values);
										else
											DBService.updateNoteById(
													Integer.valueOf(noteId),
													values);
										//������ǰactivity
										NoteEditActivity.this.finish();
										Toast.makeText(NoteEditActivity.this, "����ɹ�����",
												Toast.LENGTH_LONG).show();
									}
								}).setNegativeButton("ȡ��", null).show();

			}
		});
	}

	/**
	 * ��ʼ���༭ҳ���ֵ����������ҳ��ʱ����һ��id�Ļ�����������⣬���ݡ�
	 */
	private void initNoteEditValue() {
		// ��Intent�л�ȡid��ֵ
		long id = this.getIntent().getLongExtra("id", -1L);
		// ����д���id��ôid��=-1
		if (id != -1L) {
			// ʹ��noteId����id
			noteId = String.valueOf(id);
			// ��ѯ��id�ıʼ�
			Cursor cursor = DBService.queryNoteById((int) id);
			if (cursor.moveToFirst()) {
				// ��������ȡ����
				titleEditText.setText(cursor.getString(1));
				contentEditText.setText(cursor.getString(2));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
