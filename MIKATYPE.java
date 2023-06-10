/* ************************************************************************** */
/* 美佳のタイプトレーナー JAVA版ソースコード Ver2.06.01   2021/7/10           */
/*                                           Ver2.06.02   2023/3/12           */
/*                                           Ver2.06.03   2023/6/10           */
/*                                           Copy right 今村二朗              */
/*                                                                            */
/* このソースコードは 改変、転載、他ソフトの使用など自由にお使いください      */
/*                                                                            */
/* 注意事項                                                                   */
/*                                                                            */
/* グラフィック表示は640x400ドットの仮想画面に行い実座標に変換して表示してい  */
/* ます。                                                                     */
/*                                                                            */
/* JAVAでは横軸がX座標、縦軸がY座標ですがこのソースコードでは横軸がY座標      */
/* 縦軸がX座標です。                                                          */
/*                                                                            */
/* chromebook での使用を想定してポジション練習を小文字で表示してあります      */
/* 大文字にしたい場合はソースコードを変更してください                         */
/*                                                                            */
/* ************************************************************************** */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Container;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import java.util.Random;
import java.awt.Insets;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.awt.Toolkit;
import java.awt.Image;
import java.util.concurrent.Semaphore;
public class MIKATYPE extends JFrame {

	Semaphore MIKA_semaphore=new Semaphore(1); /* セマフォー獲得 */
	String MIKA_file_name_seiseki="mikatype.sei"; /* 成績ファイル名 読み込み用 */
	String MIKA_file_name_seiseki2="mikatype.sei"; /* 成績ファイル名 書き込み用 */
	String MIKA_file_name_kiroku="mikatype.log"; /* 練習時間記録ファイル名 追記用 */
	String MIKA_file_name_hayasa="mikatype.spd"; /* 最高速度記録ファイル名 追記用 */
	int MIKA_file_error_hayasa=0; /* 最高速度記録ファイル書き込みエラー =0 正常 =1 異常 */
	int MIKA_file_error_kiroku=0; /* 練習時間記録ファイル書き込みエラー =0 正常 =1 異常 */
	int MIKA_file_error_seiseki=0; /* 成績ファイル書き込みエラー =0 正常 =1 異常 */
	Procptimer MIKA_Procptimer; /* ポジション練習ガイドキー文字位置表示用タイマー */
	Procrtimer MIKA_Procrtimer; /* ランダム練習 英単語練習入力速度表示用タイマー */
	Procatimer MIKA_Procatimer; /* ローマ字練習入力速度表示用タイマー */
	Date MIKA_s_date; /* 練習開始日時 プログラム起動時に取得 練習時間記録ファイルに書き込み時使用 */
	Date MIKA_type_kiroku_date; /* 最高速度達成日時 (時分秒を含む)*/
	String MIKA_type_date; /* 最高速度達成日 一時保存エリア MIKA_type_kiroku_dateの年月日のみを保存 */
	long MIKA_st_t; /*  練習時間記録ファイル用練習開始時間ミリ秒 */
	long MIKA_lt_t; /*  練習時間記録ファイル用練習終了時間ミリ秒 */
	long 	MIKA_rt_t=0; /* 成績ファイル用合計練習時間  秒 */
	String[] MIKA_seiseki={null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null}; /* 成績データ読み込みデータ列 */
	String[]	MIKA_r_date= /* ランダム練習 最高速度達成日付 */
	{
		"        ",
		"        ",
		"        ",
		"        ",
		"        ",
		"        ",
		"        ",
		"        "
	};
	String[]	MIKA_w_date= /* 英単語練習 最高速度達成日付 */
	{
		"        ",
		"        ",
		"        ",
		"        ",
		"        ",
		"        ",
		"        "
	};
	String[]	MIKA_a_date= /* ローマ字練習 最高速度達成日付 */
	{
		"        ",
		"        "
	};
	double[]	MIKA_r_speed= /* ランダム練習 最高速度記録 */
	{
		0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
	};
	double[]	MIKA_w_speed= /* 英単語練習 最高速度記録 */
	{
		0.0,0.0,0.0,0.0,0.0,0.0,0.0
	};
	double[]	MIKA_a_speed= /* ローマ字練習 最高速度記録 */
	{
		0.0,0.0
	};
	long	MIKA_p_time=0; /* ポジション練習 累積練習時間 秒*/
	long[]	MIKA_r_time= /* ランダム練習 累積練習時間 秒 */
	{
		0,0,0,0,0,0,0,0
	};
	long[]	MIKA_w_time= /* 英単語練習 累積練習時間 秒 */
	{
		0,0,0,0,0,0,0
	};
	long[]	MIKA_a_time= /* ローマ字練習 累積練習時間 秒 */
	{
		0,0
	};
	Timer MIKA_timer=new Timer(); /* ポジション練習 ランダム練習 英単語練習 ローマ字練習用 タイマー取得 */
	String MIKA_c_pos1="1234567890"; /* キーボード 最上段 刻印文字列 */
//	String MIKA_c_pos2="QWERTYUIOP";
	String MIKA_c_pos2="qwertyuiop"; /* キーボード 上一段 刻印文字列 */
//	String MIKA_c_pos3="ASDFGHJKL;";
	String MIKA_c_pos3="asdfghjkl;"; /* キーボード ホームポジション 刻印文字列 */
//	String MIKA_c_pos4="ZXCVBNM,.";
	String MIKA_c_pos4="zxcvbnm,."; /* キーボード 下一段刻文字列印 */
	String[] MIKA_c_post={MIKA_c_pos1,MIKA_c_pos2,MIKA_c_pos3,MIKA_c_pos4}; /* キーボード刻印文字列テーブル */
//	String MIKA_h_pos1="ASDFGHJKL";
	String MIKA_h_pos1="asdfghjkl"; /* ホームポジション 練習文字列 */
//	String MIKA_h_pos2="QWERTYUIOP";
	String MIKA_h_pos2="qwertyuiop"; /* 上一段 練習文字列 */
//	String MIKA_h_pos3="ASDFGHJKLQWERTYUIOP";
	String MIKA_h_pos3="asdfghjklqwertyuiop"; /* ホームポジション＋上一段 練習文字列 */
//	String MIKA_h_pos4="ZXCVBNM";
	String MIKA_h_pos4="zxcvbnm"; /* 下一段 練習文字列 */
//	String MIKA_h_pos5="ASDFGHJKLZXCVBNM";
	String MIKA_h_pos5="asdfghjklzxcvbnm"; /* ホームポジション＋下一段 練習文字列 */
//	String MIKA_h_pos6="ASDFGHJKLQWERTYUIOPZXCVBNM";
	String MIKA_h_pos6="asdfghjklqwertyuiopzxcvbnm"; /* ホームポジション＋上一段＋下一段 練習文字列 */
	String MIKA_h_pos7="1234567890"; /* 数字 練習文字列 */
//	String MIKA_h_pos8="ASDFGHJKLQWERTYUIOPZXCVBNM1234567890";
	String MIKA_h_pos8="asdfghjklqwertyuiopzxcvbnm1234567890"; /* 全段 練習文字列 */
	String[] MIKA_h_pos={MIKA_h_pos1,MIKA_h_pos2,MIKA_h_pos3,MIKA_h_pos4,MIKA_h_pos5,MIKA_h_pos6,MIKA_h_pos7,MIKA_h_pos8}; /* ポジション練習 ランダム練習 練習文字列テーブル */
	int[] MIKA_p_count=null; /* 練習回数配列 アドレス */
	int[] MIKA_p_count_position={0,0,0,0,0,0,0,0}; /* ポジション練習 練習回数 */
	int[] MIKA_p_count_random={0,0,0,0,0,0,0,0}; /* ランダム練習 練習回数 */
	int[] MIKA_p_count_word={0,0,0,0,0,0,0}; /* 英単語練習 練習回数 */
	int[] MIKA_p_count_romaji={0,0}; /* ローマ字練習練習回数 */
	String	MIKA_char_table; /* 練習文字列テーブル アドレス */
	String[] MIKA_word_table; /* 練習単語テーブルアドレス */
	Color MIKA_magenta=new Color(128,32,128); /* 濃いめのマゼンタ */
	Color MIKA_green=new Color(0,128,0); /* 濃いめのグリーン */
	Color MIKA_blue=new Color(0,0,128); /* 濃いめの青 */
	Color MIKA_cyan=new Color(0,128,128); /* 濃いめのシアン */
	Color MIKA_orange=new Color(128,32,0); /* 濃いめのオレンジ */
	Color MIKA_red=new Color(128,0,0); /* 濃いめの赤 */
	Color MIKA_color_position_err=new Color(192,0,0); /* ポジション練習のエラー文字の赤 */
	Color MIKA_bk_color=Color.white; /* 背景の色 */
	Color MIKA_finger_color=new Color(255,191,63); /* 指の色 */
	Color MIKA_nail_color=new Color(255,255,191); /* 指の爪の色 */
//	Color MIKA_nail_color=new Color(255,0,0); /* 指の爪の色 */
	Color MIKA_color_text_under_line=new Color(0,0,255); /* ランダム練習 英単語練習 ローマ字練習の下線表示色 */
	Color MIKA_color_romaji=new Color(0,0,255); /* ローマ字表記 文字表示色*/
	Color MIKA_color_romaji_err=new Color(255,0,0); /* ローマ字表記 エラー文字背景表示色 */
	Color MIKA_color_romaji_under_line=new Color(0,0,255); /* ローマ字表記 下線表示色 */
	String MIKA_type_kind_mes=null; /* 練習項目名 */
	double[] MIKA_type_speed_record=null; /* 最高速度記録配列アドレス */
	String[]	MIKA_type_date_record=null; /* 最高速度達成日配列アドレス */
	long[]	MIKA_type_time_record=null; /* 累積練習時間配列 アドレス */
	long MIKA_type_start_time=0; /* ポジション練習 ランダム練習 英単語練習 ローマ字練習 練習開始時間 ミリ秒 */
	long MIKA_type_end_time=0; /* ポジション練習 ランダム練習 英単語練習 ローマ字練習 練習終了時間 ミリ秒 */
	double MIKA_type_speed_time=0.0; /* 前回 練習経過時間 秒 */
	double MIKA_ttype_speed_time=0.0; /* 今回 練習経過時間 秒 */
	double MIKA_type_speed=0.0; /* ランダム練習 英単語練習 ローマ字練習 の文字入力速度 */
	double MIKA_type_speed2=0.0; /* ローマ字入力時の打鍵速度 */
	int	MIKA_position_limit=60; /* ポジション練習 練習文字数 */
	double MIKA_random_key_limit=60.0; /* ランダム練習 英単語練習 ローマ字練習 キー入力の 制限時間 秒 */
	double MIKA_random_key_limit2=60.0; /* ランダム練習 英単語練習 ローマ字練習 タイマーの 制限時間 秒 */
	long	MIKA_random_time_interval=1000; /* ランダム練習 英単語練習 ローマ字練習 一秒タイマー ミリ秒 */
	int MIKA_type_syuryou_flag=0; /* 練習終了時の記録更新フラグ =0 更新せず =1 前回の入力速度が0.0の時の記録更新 =2 前回の記録が0.0より大きい時の記録更新 */
	int	MIKA_char_position=0; /* 練習文字番号 ポジション練習 ランダム練習にてランダムに文字を選択する時のポインター */
	char MIKA_key_char=0; /* 練習文字 */
	char MIKA_guide_char=0; /* ガイドキー文字 */
	char MIKA_err_char=0; /* エラー文字 */
	int	MIKA_type_count=0; /* 入力文字数カウンター */
	int MIKA_w_count=0; /* ひらがな入力文字数カウンター */
	int	MIKA_type_err_count=0; /* エラー入力文字数カウンター */
	int MIKA_c_p1=0,MIKA_c_p2=0; /* ランダム練習 英単語練習 ローマ字練習の練習文字ポインター */
	int MIKA_err_char_flag=0; /* エラー入力フラグ */
	int MIKA_time_start_flag=0; /* 時間計測開始フラグ =0 開始前 =1 測定中 */
	String MIKA_romaji=null; /* ひらがなのローマ字表記 */
	int MIKA_romaji_length=0; /* ひらがなのローマ字表記の文字数 */
	String MIKA_romaji2=null; /* ひらがなのローマ字の別表記 */
	int MIKA_romaji_length2=0; /* ひらがなのローマ字の別表記の文字数 */
	char MIKA_key_char2=0; /* ひらがなローマ字別表記の練習文字 */
	int MIKA_r_count=0; /* ひらがな一文字内のローマ字表記文字カウンター */
	double MIKA_random_scale=1.0; /* ランダム練習 英単語練習 ローマ字練習の文字表示倍率 */
	double MIKA_romaji_scale=2.0; /* ローマ字練習のローマ字表記の文字表示倍率 */ /* 2023/2/25追加 */
	double MIKA_romaji_bk_scale=1.4; /* ローマ字練習のローマ字表記の背景の四角表示倍率 */ /* 2023/2/25追加 */
	int MIKA_max_x_flag=0;/* 画面表示 縦行数モード =0 25行 =1 20行 */
	int MIKA_max_y_flag=0;/* 画面表示 横文半角カラム数モード =0 80カラム =1 64カラム */
	int MIKA_width_x=16; /* 全角文字 半角文字 縦方向ドット数 */
	int MIKA_width_y=8; /* 半角文字 横方向ドット数 */
	int MIKA_practice_end_flag=0; /* 練習実行中フラグ =0 練習中 =1 終了中 ESCによる終了も含む */
	int MIKA_key_guide_flag=0; /* キーガイドメッセージ表示フラグ =0 表示なし =1 次回はキーガイドを表示を消して練習 =2次回はキーガイドを表示を消して練習 */
	int MIKA_menu_kind_flag=0; /* =1 キーガイド表示あり =3 キーガイド表示無し */
	int MIKA_key_guide_on=1; /* 定数 キーガイド表示あり */
	int MIKA_key_guide_off=3; /* 定数 キーガイド表示無し */
	int MIKA_type_end_flag=0; /* 練習終了フラグ =0 ESCによる終了 =1 60文字入力による終了 */
	int[]	MIKA_pasciix=new int[26]; /* 英文字 A～Zに対応するキーのx座標 */
	int[]	MIKA_pasciiy=new int[26]; /* 英文字 A～Zに対応するキーのy座標 */
	int[]	MIKA_pnumberx=new int[10]; /* 数字 0～1に対応するキーのx座標 */
	int[]	MIKA_pnumbery=new int[10]; /* 数字 0～1に対応するキーのy座標 */
	String	MIKA_mes0="●●●  美佳のタイプトレーナー  ●●●";
	String	MIKA_mes0a="●●●  美佳のタイプトレーナー ポジション練習　●●●";
	String	MIKA_mes0b="●●●  美佳のタイプトレーナー ランダム練習　●●●";
	String	MIKA_mes0c="●●●  美佳のタイプトレーナー 英単語練習　●●●";
	String	MIKA_mes0d="●●●  美佳のタイプトレーナー ローマ字練習　●●●";
	String	MIKA_mesta="●●●  美佳のタイプトレーナー %s　●●●";
	String	MIKA_mestb="●● 美佳のタイプトレーナー ポジション練習 %s ●●";
	String	MIKA_mestc="●● 美佳のタイプトレーナー ランダム練習 %s ●●";
	String 	MIKA_mesi1="もう一度練習するときはリターンキーまたは、Enterキーを押してください";
	String	MIKA_mesi2="メニューに戻るときはESCキーを押してください";
	String	MIKA_mesi3="おめでとう、記録を更新しました";
	String	MIKA_abort_mes="ESCキーを押すと中断します";
	String	MIKA_return_mes="ESCキーを押すとメニューに戻ります";
	String	MIKA_key_type_mes="のキーを打ちましょうね．．";
	String	MIKA_keymes1="ｽﾍﾟｰｽを押すとｷｰｶﾞｲﾄﾞを消去します";
	String	MIKA_keymes2="ｽﾍﾟｰｽを押すとｷｰｶﾞｲﾄﾞを表示します";
	String	MIKA_keymes3="この次は、スペースキーを押してキーガイドの表示を消して練習してみましょうね";
	String	MIKA_keymes4="この次は、スペースキーを押してキーガイドを表示して練習してみましょうね";
	String	MIKA_mest2="練習項目           タイプ速度　文字／分       達成日       累積練習時間";
	String  MIKA_menu_mes_s[]={ /* 初期メニュー メニュー項目 */
		"ポジション練習",
		"ランダム練習",
		"英単語練習",
		"ローマ字練習",
		"成績",
		"終了",
	};
	int MIKA_menu_cord_s[][]={ /* 初期 メニュー項目表示位置 x座標 y座標 */
		{3*14,20*8},
		{5*14,20*8},
		{7*14,20*8},
		{9*14,20*8},
		{11*14,20*8},
		{13*14,20*8}
	};
	int MIKA_menu_s_sel_flag[]={ /* 初期メニュー メニュー項目選択フラグ */
		0,0,0,0,0,0};
	int MIKA_menu_s_function[]={ /* 初期メニュー 機能番号 */
		21,22,23,24,29,9999};
	String  MIKA_menu_mes[]={ /* ポジション練習 ランダム練習 メニュー項目 */
		"ホームポジション",
		"上一段",
		"ホームポジション＋上一段",
		"下一段",
		"ホームポジション＋下一段",
		"ホームポジション＋上一段＋下一段",
		"数字",
		"全段",
		"メニューに戻る"
	};
	int MIKA_menu_cord[][]={ /* ポジション練習 ランダム練習 英単語練習 ローマ字練習 メニュー項目表示位置 x座標 y座標 */
		{2*14,20*8},
		{4*14,20*8},
		{6*14,20*8},
		{8*14,20*8},
		{10*14,20*8},
		{12*14,20*8},
		{14*14,20*8},
		{16*14,20*8},
		{18*14,20*8}
	};
	int MIKA_position_menu_function[]={ /* ポジション練習 機能番号 */
		401,402,403,404,405,406,407,408,9001};
	int MIKA_position_sel_flag[]={ /* ポジション練習 メニュー項目選択フラグ */
		0,0,0,0,0,0,0,0,0};
	int MIKA_random_menu_function[]={ /* ランダム練習 機能番号 */
		501,502,503,504,505,506,507,508,9001};
	int MIKA_random_sel_flag[]={ /* ランダム練習 メニュー項目選択フラグ */
		0,0,0,0,0,0,0,0,0};
	String  MIKA_menu_mes_w[]={ /* 英単語練習 メニュー項目 */
		"基本英単語練習",
		"ＭＳＤＯＳコマンド練習",
		"Ｃ言語練習",
		"パスカル練習",
		"フォートラン練習",
		"ＢＡＳＩＣ練習",
		"８０８６アセンブラ練習",
		"メニューに戻る"
	};
	int MIKA_word_menu_function[]={ /* 英単語練習 機能番号 */
		601,602,603,604,605,606,607,9001};
	int MIKA_word_sel_flag[]={ /* 英単語練習 メニュー項目選択フラグ */
		0,0,0,0,0,0,0,0};
	String  MIKA_menu_mes_r[]={ /* ローマ字練習 メニュー項目 */
		"ローマ字ランダム練習",
		"ローマ字単語練習",
		"メニューに戻る"
	};
	int MIKA_romaji_menu_function[]={ /* ローマ字練習 機能番号 */
		701,702,9001};
	int MIKA_romaji_sel_flag[]={ /* ローマ字練習 メニュー項目選択フラグ */
		0,0,0};	
	int MIKA_fngpoint[][]={ /* 指表示位置 x 座標 y 座標 表示幅 */
		{21*16+8,10*8+6,3*8+2}, /* 左手小指 */
		{20*16+2,15*8,4*8}, /* 左手薬指 */
		{20*16-3,20*8,4*8}, /* 左手中指 */
		{20*16+2,25*8,4*8}, /* 左手人指し指 */
		{22*16,31*8-4,5*8}, /* 左手親指 */
		{22*16,39*8+4,5*8}, /* 右手親指 */
		{20*16+2,46*8,4*8}, /* 右手人指し指 */
		{20*16-3,51*8,4*8}, /* 右手中指 */
		{20*16+2,56*8,4*8}, /* 右手薬指 */
		{21*16+8,61*8,3*8+2} /* 右手小指 */
	};
	int	MIKA_t_line=7; /* ランダム練習 英単語練習 ローマ字練習 練習テキスト表示開始行位置 */
	char[][] MIKA_chat_t=new char[10][40]; /* 練習テキスト文字 横40文字 縦10行 */
	int[] MIKA_chat_yomi_t=new int[10*40]; /* 練習テキストのひらがなに対応した文字番号 */
	int MIKA_cline_x; /* ランダム練習 英単語練習 ローマ字練習 練習テキスト行数 最小=3 最大 =10 */
	int MIKA_cline_c; /* ランダム練習 英単語練習 ローマ字練習 練習テキスト 文字数 */
	int MIKA_utikiri_flag;	/* ランダム練習 英単語練習 ローマ字練習 練習テキスト打ち切りフラグ =1 全練習テキスト打ち切りによる終了 =0 60秒タイマーによる終了 */
	int MIKA_utikiri_flag2;	/* 前回速度表示時の打ち切りフラグの値 */
//	int MIKA_exec_func_no=29;
//	int MIKA_exec_func_no=21;
	int MIKA_exec_func_no=0; /* メニューの機能番号 */
	int	MIKA_type_kind_no=0; /* 練習項目番号 */
	int[]	MIKA_menu_function_table; /* メニューの機能番号テーブルアドレス */
	int[]	MIKA_sel_flag; /* 前回選択メニュー項目選択フラグアドレス */
	Dimension MIKA_win_size; /* ウィンドーサイズ */
	Insets	MIKA_insets; /* ウィンドー表示領域 */
String[] MIKA_oubun={
	"America",
	"American",
	"April",
	"British",
	"England",
	"English",
	"Europe",
	"Greek",
	"I",
	"Japan",
	"Japanese",
	"a",
	"ability",
	"able",
	"about",
	"above",
	"abroad",
	"absent",
	"accept",
	"accident",
	"account",
	"achieve",
	"acquire",
	"across",
	"act",
	"action",
	"activity",
	"add",
	"address",
	"admire",
	"admit",
	"advance",
	"advantage",
	"afraid",
	"after",
	"again",
	"against",
	"age",
	"ago",
	"agree",
	"air",
	"all",
	"allow",
	"almost",
	"alone",
	"along",
	"already",
	"also",
	"although",
	"always",
	"am",
	"among",
	"amount",
	"an",
	"ancient",
	"and",
	"angry",
	"animal",
	"another",
	"answer",
	"any",
	"anybody",
	"anyone",
	"anything",
	"anywhere",
	"apart",
	"appear",
	"are",
	"area",
	"around",
	"arrive",
	"art",
	"artist",
	"as",
	"ask",
	"asleep",
	"at",
	"attempt",
	"attend",
	"attention",
	"attitude",
	"autumn",
	"average",
	"away",
	"back",
	"bad",
	"bag",
	"bake",
	"base",
	"be",
	"beach",
	"beautiful",
	"beauty",
	"became",
	"because",
	"become",
	"bed",
	"been",
	"before",
	"began",
	"begin",
	"behavior",
	"behind",
	"belief",
	"believe",
	"belong",
	"below",
	"bend",
	"best",
	"better",
	"between",
	"beyond",
	"big",
	"bird",
	"bit",
	"black",
	"blood",
	"blow",
	"blue",
	"body",
	"book",
	"born",
	"both",
	"box",
	"boy",
	"break",
	"bring",
	"brother",
	"brought",
	"build",
	"burn",
	"business",
	"but",
	"buy",
	"by",
	"cake",
	"call",
	"came",
	"can",
	"cannot",
	"car",
	"care",
	"career",
	"carry",
	"case",
	"catch",
	"caught",
	"cause",
	"century",
	"certain",
	"certainly",
	"chance",
	"change",
	"character",
	"characteristic",
	"child",
	"children",
	"choice",
	"choose",
	"city",
	"civilization",
	"class",
	"clear",
	"clock",
	"close",
	"cloud",
	"cold",
	"college",
	"color",
	"come",
	"common",
	"communication",
	"company",
	"complete",
	"concern",
	"condition",
	"consider",
	"continue",
	"control",
	"cool",
	"could",
	"count",
	"country",
	"course",
	"cover",
	"create",
	"creature",
	"culture",
	"cut",
	"danger",
	"dark",
	"date",
	"day",
	"deal",
	"death",
	"decide",
	"deep",
	"degree",
	"demand",
	"describe",
	"desire",
	"determine",
	"develop",
	"development",
	"did",
	"die",
	"difference",
	"different",
	"difficult",
	"difficulty",
	"discover",
	"discovery",
	"discuss",
	"distinguish",
	"do",
	"doctor",
	"dog",
	"done",
	"door",
	"doubt",
	"down",
	"draw",
	"dream",
	"dress",
	"drink",
	"driver",
	"drop",
	"dry",
	"during",
	"each",
	"early",
	"earth",
	"easily",
	"east",
	"easy",
	"eat",
	"economic",
	"educate",
	"education",
	"effect",
	"effort",
	"either",
	"else",
	"empty",
	"end",
	"energy",
	"enjoy",
	"enough",
	"enter",
	"environment",
	"escape",
	"especially",
	"even",
	"event",
	"ever",
	"every",
	"everyone",
	"everything",
	"exactly",
	"example",
	"except",
	"excuse",
	"exist",
	"existence",
	"expect",
	"experience",
	"explain",
	"express",
	"expression",
	"eye",
	"face",
	"fact",
	"fail",
	"fall",
	"family",
	"famous",
	"far",
	"fashion",
	"fast",
	"father",
	"favorite",
	"fear",
	"feel",
	"feeling",
	"feet",
	"fellow",
	"felt",
	"few",
	"field",
	"fight",
	"finally",
	"find",
	"fine",
	"fire",
	"first",
	"five",
	"floor",
	"flower",
	"fog",
	"follow",
	"food",
	"foot",
	"for",
	"force",
	"foreign",
	"form",
	"forward",
	"found",
	"four",
	"free",
	"freedom",
	"fresh",
	"friend",
	"from",
	"front",
	"full",
	"fun",
	"future",
	"gain",
	"game",
	"garden",
	"gave",
	"general",
	"generally",
	"generation",
	"get",
	"gift",
	"girl",
	"give",
	"given",
	"glass",
	"go",
	"gone",
	"good",
	"got",
	"government",
	"great",
	"green",
	"ground",
	"group",
	"grow",
	"habit",
	"had",
	"half",
	"hand",
	"happen",
	"happy",
	"hard",
	"hardly",
	"has",
	"have",
	"he",
	"head",
	"hear",
	"heard",
	"heart",
	"help",
	"her",
	"here",
	"hide",
	"high",
	"him",
	"himself",
	"his",
	"history",
	"hold",
	"hole",
	"home",
	"hope",
	"hour",
	"house",
	"how",
	"however",
	"human",
	"hundred",
	"idea",
	"ideal",
	"idle",
	"if",
	"ill",
	"imagine",
	"importance",
	"important",
	"impossible",
	"in",
	"include",
	"increase",
	"indeed",
	"individual",
	"industry",
	"influence",
	"information",
	"injure",
	"inside",
	"instance",
	"instead",
	"intellectual",
	"interest",
	"interested",
	"interesting",
	"into",
	"introduce",
	"invent",
	"invite",
	"is",
	"it",
	"its",
	"itself",
	"job",
	"join",
	"joy",
	"judge",
	"jump",
	"just",
	"keep",
	"kept",
	"key",
	"kill",
	"kind",
	"knew",
	"know",
	"knowledge",
	"known",
	"labor",
	"lack",
	"lady",
	"land",
	"language",
	"large",
	"last",
	"late",
	"later",
	"laugh",
	"law",
	"lay",
	"lead",
	"learn",
	"least",
	"leave",
	"left",
	"less",
	"lesson",
	"let",
	"letter",
	"library",
	"lie",
	"life",
	"light",
	"like",
	"likely",
	"limit",
	"line",
	"listen",
	"literature",
	"little",
	"live",
	"local",
	"lonely",
	"long",
	"look",
	"lose",
	"lost",
	"lot",
	"loud",
	"love",
	"lovely",
	"low",
	"machine",
	"made",
	"mail",
	"main",
	"major",
	"make",
	"man",
	"manner",
	"many",
	"mark",
	"mass",
	"material",
	"matter",
	"may",
	"me",
	"mean",
	"meaning",
	"meant",
	"measure",
	"meet",
	"member",
	"men",
	"mental",
	"merely",
	"method",
	"might",
	"mile",
	"million",
	"mind",
	"minute",
	"modern",
	"moment",
	"money",
	"month",
	"moon",
	"more",
	"morning",
	"most",
	"mother",
	"move",
	"movement",
	"much",
	"music",
	"must",
	"my",
	"myself",
	"name",
	"nation",
	"natural",
	"nature",
	"near",
	"nearly",
	"necessary",
	"need",
	"neighbor",
	"never",
	"new",
	"newspaper",
	"next",
	"nice",
	"night",
	"no",
	"noise",
	"none",
	"nor",
	"normal",
	"not",
	"nothing",
	"notice",
	"novel",
	"now",
	"number",
	"object",
	"occur",
	"of",
	"off",
	"offer",
	"often",
	"old",
	"on",
	"once",
	"one",
	"only",
	"open",
	"opinion",
	"opportunity",
	"or",
	"order",
	"ordinary",
	"original",
	"other",
	"ought",
	"our",
	"ourselves",
	"out",
	"outside",
	"over",
	"own",
	"paper",
	"parent",
	"part",
	"particular",
	"particularly",
	"pass",
	"past",
	"path",
	"pay",
	"peace",
	"people",
	"perhaps",
	"period",
	"person",
	"personal",
	"philosophy",
	"phone",
	"photo",
	"physical",
	"pick",
	"picture",
	"piece",
	"pity",
	"place",
	"plan",
	"play",
	"please",
	"pleasure",
	"poetry",
	"point",
	"political",
	"poor",
	"popular",
	"population",
	"position",
	"possible",
	"post",
	"power",
	"practical",
	"practice",
	"prefer",
	"present",
	"prevent",
	"private",
	"probably",
	"problem",
	"process",
	"produce",
	"progress",
	"prove",
	"provide",
	"public",
	"purpose",
	"put",
	"quality",
	"question",
	"quick",
	"quiet",
	"quite",
	"radio",
	"rain",
	"rate",
	"rather",
	"reach",
	"read",
	"reader",
	"reading",
	"real",
	"realize",
	"really",
	"reason",
	"receive",
	"recognize",
	"record",
	"regard",
	"relation",
	"remain",
	"remember",
	"require",
	"respect",
	"rest",
	"result",
	"return",
	"rich",
	"right",
	"river",
	"road",
	"room",
	"rule",
	"run",
	"sad",
	"safe",
	"said",
	"same",
	"satisfy",
	"save",
	"saw",
	"say",
	"school",
	"science",
	"scientific",
	"scientist",
	"sea",
	"season",
	"second",
	"see",
	"seem",
	"seen",
	"sense",
	"separate",
	"serious",
	"service",
	"set",
	"several",
	"shall",
	"share",
	"she",
	"short",
	"should",
	"show",
	"sick",
	"side",
	"sight",
	"simple",
	"simply",
	"since",
	"sincerely",
	"single",
	"sit",
	"situation",
	"six",
	"sleep",
	"small",
	"so",
	"social",
	"society",
	"soft",
	"some",
	"someone",
	"something",
	"sometimes",
	"son",
	"soon",
	"sort",
	"sound",
	"southern",
	"space",
	"speak",
	"special",
	"speech",
	"spend",
	"spirit",
	"stage",
	"stand",
	"standard",
	"star",
	"start",
	"state",
	"stay",
	"step",
	"still",
	"stone",
	"stop",
	"story",
	"strange",
	"street",
	"strong",
	"student",
	"study",
	"subject",
	"success",
	"such",
	"suddenly",
	"suffer",
	"suggest",
	"sun",
	"suppose",
	"sure",
	"surface",
	"surprise",
	"system",
	"take",
	"taken",
	"talk",
	"tall",
	"taste",
	"teach",
	"teacher",
	"television",
	"tell",
	"ten",
	"tend",
	"term",
	"test",
	"than",
	"that",
	"the",
	"their",
	"them",
	"themselves",
	"then",
	"there",
	"therefore",
	"these",
	"they",
	"thing",
	"think",
	"this",
	"those",
	"though",
	"thought",
	"thousand",
	"three",
	"through",
	"thus",
	"time",
	"to",
	"today",
	"together",
	"told",
	"too",
	"took",
	"tool",
	"toward",
	"town",
	"train",
	"travel",
	"tree",
	"trouble",
	"true",
	"truth",
	"try",
	"turn",
	"twenty",
	"two",
	"type",
	"under",
	"understand",
	"uniform",
	"universe",
	"university",
	"unless",
	"until",
	"up",
	"upon",
	"us",
	"use",
	"used",
	"usually",
	"value",
	"various",
	"very",
	"view",
	"village",
	"visit",
	"voice",
	"wait",
	"walk",
	"want",
	"war",
	"was",
	"watch",
	"water",
	"way",
	"we",
	"week",
	"well",
	"went",
	"were",
	"what",
	"whatever",
	"when",
	"where",
	"whether",
	"which",
	"while",
	"white",
	"who",
	"whole",
	"whom",
	"whose",
	"why",
	"wife",
	"will",
	"window",
	"wish",
	"with",
	"within",
	"without",
	"woman",
	"women",
	"wonder",
	"wood",
	"word",
	"work",
	"worker",
	"world",
	"worry",
	"worth",
	"would",
	"write",
	"writer",
	"writing",
	"written",
	"wrong",
	"year",
	"yes",
	"yesterday",
	"yet",
	"you",
	"young",
	"your",
	"yourself"
};
String[] MIKA_memsdos={
	"ASSIGN",
	"BACKUP",
	"CD",
	"CHKDSK",
	"COPY",
	"CLS",
	"DATE",
	"DEL",
	"DIR",
	"DISKCOPY",
	"DUMP",
	"EXIT",
	"FORMAT",
	"MENU",
	"MKDIR",
	"MORE",
	"PATH",
	"PRINT",
	"RECOVER",
	"REN",
	"RMDIR",
	"SET",
	"SPEED",
	"SYS",
	"TIME",
	"TYPE"
};
String[] MIKA_mec={
	"auto",
	"static",
	"extern",
	"register",
	"typedef",
	"char",
	"short",
	"int",
	"long",
	"unsigned",
	"float",
	"double",
	"struct",
	"union",
	"if",
	"else",
	"while",
	"do",
	"switch",
	"case",
	"default",
	"break",
	"continue",
	"return",
	"goto",
	"define",
	"include",
	"printf",
	"fprintf",
	"scanf",
	"fopen",
	"fclose",
	"fscanf",
	"getchar",
	"putchar",
	"stdio",
	"for",
	"sprintf",
	"sscanf"
};
String[] MIKA_mepascal={
	"real",
	"integer",
	"char",
	"Boolean",
	"packed",
	"array",
	"of",
	"set",
	"file",
	"record",
	"end",
	"case",
	"nil",
	"in",
	"div",
	"mod",
	"goto",
	"begin",
	"if",
	"then",
	"else",
	"case",
	"while",
	"do",
	"repeat",
	"until",
	"for",
	"to",
	"downto",
	"with",
	"program",
	"label",
	"const",
	"type",
	"var",
	"procedure",
	"function",
	"false",
	"true",
	"text",
	"input",
	"output",
	"get",
	"put",
	"reset",
	"rewrite",
	"read",
	"readln",
	"write",
	"writeln",
	"page",
	"pack",
	"unpack"
};
String[] MIKA_mefortran={
	"GO",
	"TO",
	"ASSIGN",
	"IF",
	"DO",
	"CONTINUE",
	"PAUSE",
	"STOP",
	"CALL",
	"RETURN",
	"READ",
	"WRITE",
	"BACKSPACE",
	"REWIND",
	"ENDFILE",
	"DIMENSION",
	"COMMON",
	"EQUIVALENCE",
	"INTEGER",
	"REAL",
	"LOGICAL",
	"DOUBLE",
	"PRECISION",
	"COMPLEX",
	"EXTERNAL",
	"DATA",
	"FORMAT",
	"FUNCTION",
	"SUBROUTINE",
	"BLOCK",
	"DATA",
	"END"
};
String[] MIKA_mebasic={
	"AUTO",
	"BEEP",
	"BLOAD",
	"BSAVE",
	"CALL",
	"CHAIN",
	"CIRCLE",
	"CLEAR",
	"CLOSE",
	"CLS",
	"COLOR",
	"COMMON",
	"COM",
	"ON",
	"OFF",
	"STOP",
	"CONSOLE",
	"CONT",
	"COPY",
	"DATA",
	"DEF",
	"DEFINT",
	"DEFSNG",
	"DEFDBL",
	"DEFSTR",
	"SEG",
	"USR",
	"DELETE",
	"DIM",
	"DRAW",
	"EDIT",
	"END",
	"ERASE",
	"ERROR",
	"FIELD",
	"FILES",
	"LFILES",
	"FOR",
	"TO",
	"STEP",
	"NEXT",
	"GET",
	"GOSUB",
	"GOTO",
	"RETURN",
	"HELP",
	"IF",
	"THEN",
	"ELSE",
	"INPUT",
	"WAIT",
	"KEY",
	"LIST",
	"KILL",
	"KINPUT",
	"KPLOAD",
	"LET",
	"LIST",
	"LLIST",
	"LINE",
	"LOAD",
	"LOCATE",
	"LSET",
	"RSET",
	"MERGE",
	"MON",
	"MOTOR",
	"NAME",
	"NEW",
	"OPEN",
	"OPTION",
	"BASE",
	"OUT",
	"PAINT",
	"PEN",
	"POINT",
	"POKE",
	"PRESET",
	"PRINT",
	"LPRINT",
	"USING",
	"PSET",
	"PUT",
	"RANDOMIZE",
	"READ",
	"REM",
	"RENUM",
	"RESTORE",
	"RESUME",
	"ROLL",
	"RUN",
	"SAVE",
	"SCREEN",
	"SET",
	"STOP",
	"SWAP",
	"TERM",
	"TIME",
	"TRON",
	"TROFF",
	"VIEW",
	"WAIT",
	"WHILE",
	"WEND",
	"WIDTH",
	"WINDOW",
	"WRITE"
};
String[] MIKA_me8086={
	"AAA",
	"AAD",
	"AAM",
	"AAS",
	"ADC",
	"ADD",
	"AND",
	"CALL",
	"CBW",
	"CLC",
	"CLD",
	"CLI",
	"CMC",
	"CMP",
	"CMPS",
	"CWD",
	"DAA",
	"DAS",
	"DEC",
	"DIV",
	"ESC",
	"HLT",
	"IDIV",
	"IMUL",
	"IN",
	"INC",
	"INT",
	"INTR",
	"INTO",
	"IRET",
	"JA",
	"JNBE",
	"JAE",
	"JNB",
	"JB",
	"JNAE",
	"JBE",
	"JNA",
	"JC",
	"JCXZ",
	"JE",
	"JZ",
	"JG",
	"JNLE",
	"JGE",
	"JNL",
	"JL",
	"JNGE",
	"JLE",
	"JNG",
	"JMP",
	"JNC",
	"JNE",
	"JNZ",
	"JNO",
	"JNP",
	"JPO",
	"JNS",
	"JO",
	"JP",
	"JPE",
	"JS",
	"LAHF",
	"LDS",
	"LOCK",
	"LODS",
	"LOOP",
	"LOOPE",
	"LOOPZ",
	"LOOPNE",
	"LOOPNZ",
	"LEA",
	"LES",
	"NMI",
	"MOV",
	"MOVS",
	"MOVSB",
	"MOVSW",
	"MUL",
	"NEG",
	"NOP",
	"NOT",
	"OR",
	"OUT",
	"POP",
	"POPF",
	"PUSH",
	"PUSHF",
	"RCL",
	"RCR",
	"REP",
	"REPE",
	"REPZ",
	"REPNE",
	"REPNZ",
	"RET",
	"ROL",
	"ROR",
	"SAHF",
	"SAL",
	"SHL",
	"SAR",
	"SBB",
	"SCAS",
	"SHR",
	"SINGLESTEP",
	"STC",
	"STD",
	"STI",
	"STOS",
	"SUB",
	"TEST",
	"WAIT",
	"XCHG",
	"XLAT",
	"XOR",
	"CS",
	"DS",
	"SS",
	"ES",
	"AX",
	"BX",
	"CX",
	"DX",
	"AH",
	"AL",
	"BH",
	"BL",
	"CH",
	"CL",
	"DH",
	"DL",
	"SP",
	"BP",
	"SI",
	"DI",
	"DB",
	"DW",
	"DD",
	"DUP",
	"SEGMENT",
	"ENDS",
	"ORG",
	"GROUP",
	"ASSUME",
	"NOTHING",
	"PROC",
	"ENDP",
	"LABEL",
	"EQU",
	"PURGE",
	"NAME",
	"PUBLIC",
	"EXTRN",
	"END",
	"RECORD",
	"PARA",
	"BYTE",
	"WORD",
	"PAGE",
	"INPAGE",
	"COMMON",
	"AT",
	"STACK",
	"MEMORY",
	"SEG",
	"PTR",
	"THIS",
	"TYPE",
	"OFFSET",
	"LENGTH",
	"SIZE",
	"WIDTH"
};
	String[][] MIKA_w_seq={MIKA_oubun,MIKA_memsdos,MIKA_mec,MIKA_mepascal,MIKA_mefortran,MIKA_mebasic,MIKA_me8086}; /* 英単語練習 練習単語テーブル */
// String[] MIKA_romaji_tango_table={
//	"ししし",
//	"ししし",
//	"しししし"
//	};
// String[] MIKA_romaji_tango_table={
//	"しちつふ",
//	"らりるれろじ"
//	};
String[] MIKA_romaji_tango_table={
	"あいくるしい",
	"あいご",
	"あいさつ",
	"あいず",
	"あいて",
	"あいどく",
	"あいにく",
	"あいま",
	"あいよう",
	"あおもの",
	"あからさまに",
	"あき",
	"あきたりない",
	"あきない",
	"あけてもくれても",
	"あけぼの",
	"あげくのはてに",
	"あこがれ",
	"あさ",
	"あさい",
	"あさがお",
	"あざわらう",
	"あしからず",
	"あした",
	"あしでまとい",
	"あす",
	"あせまみれ",
	"あたかも",
	"あたためる",
	"あつい",
	"あつかましい",
	"あつさ",
	"あつみ",
	"あてこすり",
	"あとかたもなく",
	"あとずさり",
	"あなた",
	"あひる",
	"あぶらあせ",
	"あほうどり",
	"あまおと",
	"あまがえる",
	"あまやどり",
	"あみだす",
	"あめ",
	"あやうく",
	"あやまり",
	"あゆみよる",
	"あらいざらい",
	"あらし",
	"あらわす",
	"あらんかぎり",
	"ありえない",
	"ありのままに",
	"あれくるう",
	"あれほど",
	"あわてふためく",
	"あんき",
	"あんしん",
	"あんてい",
	"いいあらわす",
	"いいつける",
	"いいふらす",
	"いいん",
	"いう",
	"いかり",
	"いがみあう",
	"いきおいこんで",
	"いきがい",
	"いきごみ",
	"いきる",
	"いけん",
	"いさましい",
	"いさめる",
	"いざというとき",
	"いし",
	"いす",
	"いずれ",
	"いずれにしても",
	"いそがしい",
	"いたいたしい",
	"いたたまれない",
	"いちはやく",
	"いつしか",
	"いと",
	"いどう",
	"いぬ",
	"いのち",
	"いまいましい",
	"いまだに",
	"いまなお",
	"いみ",
	"いろいろ",
	"いんき",
	"いんぼう",
	"うえき",
	"うかぶ",
	"うきあしだつ",
	"うきぶくろ",
	"うしなう",
	"うたぐりぶかい",
	"うちあける",
	"うちたてる",
	"うつむく",
	"うつりぎ",
	"うなだれる",
	"うぬぼれ",
	"うまれつき",
	"うまれる",
	"うみ",
	"うめあわせる",
	"うるおす",
	"うわのそら",
	"うんせい",
	"うんめい",
	"えいが",
	"えいご",
	"えいせい",
	"えいぞう",
	"えいゆう",
	"えいよう",
	"えがお",
	"えこひいき",
	"えだ",
	"えだわかれ",
	"えつらん",
	"えりまき",
	"えんがん",
	"えんげき",
	"えんだん",
	"えんにち",
	"えんばん",
	"えんぴつ",
	"おいしげる",
	"おいそれと",
	"おうえん",
	"おうふく",
	"おうぼ",
	"おうむがえし",
	"おおきい",
	"おおよそ",
	"おかあさん",
	"おきてがみ",
	"おきみやげ",
	"おきもの",
	"おくのて",
	"おくりもの",
	"おさえる",
	"おさなごころ",
	"おさめる",
	"おしあう",
	"おしはかる",
	"おそるおそる",
	"おそれいる",
	"おちつきはらう",
	"おとしいれる",
	"おとなびた",
	"おどろき",
	"おどろく",
	"おなじ",
	"おのずから",
	"おはよう",
	"おびきよせる",
	"おぼえて",
	"おもいあたる",
	"おもいやり",
	"おもさ",
	"おやこ",
	"おりあう",
	"かいが",
	"かいけつ",
	"かいこん",
	"かいしん",
	"かいせつ",
	"かいそう",
	"かいたく",
	"かいだん",
	"かいてき",
	"かいはつ",
	"かいもく",
	"かえりみる",
	"かおいろ",
	"かおみしり",
	"かがく",
	"かがやかしい",
	"かがやき",
	"かきあつめる",
	"かきそえる",
	"かきとめる",
	"かきわける",
	"かくげん",
	"かくご",
	"かくしん",
	"かくじつ",
	"かくだい",
	"かくとく",
	"かぐ",
	"かけいぼ",
	"かけはなれる",
	"かこ",
	"かさばる",
	"かしきり",
	"かせき",
	"かそう",
	"かぞく",
	"かたい",
	"かたくるしい",
	"かたむける",
	"かてい",
	"かなしみ",
	"かねつ",
	"かのう",
	"かのうせい",
	"かばう",
	"かべ",
	"かぼそい",
	"かめい",
	"かろう",
	"かろうじて",
	"かわ",
	"かわりはてる",
	"かんかく",
	"かんかつ",
	"かんしん",
	"かんぜい",
	"かんぜん",
	"かんそう",
	"かんたん",
	"かんどう",
	"かんぱい",
	"かんよう",
	"かんり",
	"がいかん",
	"がいこう",
	"がいとう",
	"がいねん",
	"がくしき",
	"がくもん",
	"がんぼう",
	"きえる",
	"きおく",
	"きかい",
	"きかく",
	"きがかり",
	"ききだす",
	"きけん",
	"きげん",
	"きごう",
	"きしべ",
	"きせき",
	"きせつ",
	"きそ",
	"きそうてんがい",
	"きたえる",
	"きてん",
	"きねん",
	"きのどく",
	"きふ",
	"きぼう",
	"きまぐれ",
	"きみ",
	"きもち",
	"きもをひやす",
	"きらく",
	"きらびやか",
	"きりぬける",
	"きりもり",
	"きろく",
	"きをとりなおす",
	"きんようび",
	"ぎかい",
	"ぎこちない",
	"ぎじ",
	"ぎろん",
	"ぎんが",
	"くいあらためる",
	"くいさがる",
	"くいとめる",
	"くうき",
	"くうそう",
	"くうふく",
	"くがく",
	"くぎり",
	"くしん",
	"くじける",
	"くすだま",
	"くだもの",
	"くちかず",
	"くちぶり",
	"くつがえる",
	"くふう",
	"くみあい",
	"くも",
	"くもりぞら",
	"くやしい",
	"くよくよする",
	"くらい",
	"くるぶし",
	"くろう",
	"けいえい",
	"けいかく",
	"けいけん",
	"けいげん",
	"けいこく",
	"けいざい",
	"けいしき",
	"けいじ",
	"けいぞく",
	"けいと",
	"けしき",
	"けつい",
	"けつろん",
	"けむり",
	"けんさ",
	"けんちく",
	"けんとう",
	"けんにん",
	"けんめい",
	"げか",
	"げかい",
	"げきど",
	"げきれい",
	"げつようび",
	"げんいん",
	"げんかい",
	"げんかん",
	"げんき",
	"げんこう",
	"げんさく",
	"げんざい",
	"げんし",
	"げんじつ",
	"げんだい",
	"げんてん",
	"げんどう",
	"げんみつ",
	"げんり",
	"こうえい",
	"こうか",
	"こうかい",
	"こうがくしん",
	"こうぎ",
	"こうけい",
	"こうしき",
	"こうじつ",
	"こうせい",
	"こうそく",
	"こうとくしん",
	"こうどう",
	"こうふく",
	"こうへい",
	"こうぼ",
	"こうやく",
	"こえ",
	"こえる",
	"こくさい",
	"こくさん",
	"こくない",
	"こくふく",
	"こくみん",
	"こくりつ",
	"こころ",
	"こころあたり",
	"こころない",
	"こころのこり",
	"こころぼそい",
	"こころみ",
	"こしかけ",
	"こじん",
	"こせい",
	"こたえる",
	"こてん",
	"ことかく",
	"ことごとく",
	"ことば",
	"こどうぐ",
	"こどく",
	"こども",
	"こんざつ",
	"こんなん",
	"こんにちは",
	"こんばんは",
	"こんらん",
	"ごうがい",
	"ごうりてき",
	"ごかい",
	"ごがく",
	"ごご",
	"さいきん",
	"さいく",
	"さいげつ",
	"さいげん",
	"さいご",
	"さいさん",
	"さいじつ",
	"さいばい",
	"さいよう",
	"さきみだれる",
	"さくねん",
	"さくもつ",
	"さしあたり",
	"さしさわり",
	"さしとめる",
	"さしのべる",
	"さようなら",
	"さりげなく",
	"さんか",
	"さんかく",
	"ざせき",
	"しあい",
	"しあげ",
	"しかい",
	"しかし",
	"しかしながら",
	"しきべつ",
	"しぐさ",
	"しけん",
	"しごと",
	"しざい",
	"しずめる",
	"しせい",
	"しぜん",
	"したじき",
	"したて",
	"しつぎ",
	"しつげん",
	"しつもん",
	"してい",
	"してん",
	"しどう",
	"しほん",
	"しよう",
	"しらせる",
	"しらべる",
	"しわよせ",
	"しんい",
	"しんか",
	"しんがく",
	"しんせつ",
	"しんぞう",
	"しんねん",
	"しんぱい",
	"しんぴ",
	"しんぶん",
	"しんゆう",
	"しんよう",
	"しんらい",
	"しんり",
	"しんろ",
	"じかん",
	"じけん",
	"じだい",
	"じだいおくれ",
	"じつえん",
	"じどう",
	"じまん",
	"じむ",
	"じんぶつ",
	"すあし",
	"すいえい",
	"すいせい",
	"すいぞくかん",
	"すいへいせん",
	"すいり",
	"すがお",
	"すききらい",
	"すぐさま",
	"すでに",
	"すなお",
	"すべりだし",
	"すまい",
	"すれちがう",
	"ずあん",
	"せいかい",
	"せいかく",
	"せいかつ",
	"せいけつ",
	"せいげん",
	"せいさく",
	"せいじ",
	"せいすう",
	"せいせき",
	"せいだい",
	"せいとん",
	"せいひん",
	"せいふ",
	"せいふく",
	"せいぶん",
	"せいめい",
	"せいもん",
	"せかい",
	"せきこむ",
	"せきにん",
	"せけん",
	"せつめい",
	"せつやく",
	"せなか",
	"せんげん",
	"せんこく",
	"せんせい",
	"せんたく",
	"せんにん",
	"せんぱい",
	"せんべつ",
	"せんもん",
	"せんれん",
	"ぜんい",
	"ぜんご",
	"ぜんしん",
	"ぜんと",
	"そうい",
	"そうごう",
	"そうさく",
	"そうじ",
	"そうだん",
	"そうび",
	"そうりつ",
	"そくじ",
	"そくせき",
	"そこく",
	"そこなわれる",
	"そしき",
	"そち",
	"そなえつける",
	"そなえる",
	"そまつ",
	"そもそも",
	"そよかぜ",
	"そんざい",
	"そんしつ",
	"ぞうせつ",
	"たいおう",
	"たいけつ",
	"たいけん",
	"たいこう",
	"たいさく",
	"たいせき",
	"たいわ",
	"たえまなく",
	"たおれる",
	"たしなめる",
	"たずねる",
	"たたえる",
	"たちおくれる",
	"たちなおる",
	"たちむかう",
	"たどる",
	"たのみ",
	"たまたま",
	"たまりかねて",
	"ためす",
	"ためらう",
	"たよりない",
	"たんねんに",
	"だいがく",
	"だいじん",
	"だいたい",
	"だいどころ",
	"だいひつ",
	"だいり",
	"だかい",
	"だれも",
	"だんあつ",
	"だんげん",
	"だんぺん",
	"ちい",
	"ちいさい",
	"ちかい",
	"ちくせき",
	"ちこく",
	"ちちおや",
	"ちり",
	"ちんもく",
	"ついでに",
	"つかいはたす",
	"つくえ",
	"つくる",
	"つけくわえる",
	"つごう",
	"つなげる",
	"つぶぞろい",
	"つぶやく",
	"つまはじき",
	"つみたてる",
	"つめかける",
	"ていせい",
	"てがみ",
	"てきせつ",
	"てきにん",
	"てぎわよく",
	"てつどう",
	"てにあまる",
	"てもと",
	"てんき",
	"てんこう",
	"てんぶん",
	"できばえ",
	"でも",
	"でんごん",
	"でんせつ",
	"でんとう",
	"とうじ",
	"とうべん",
	"とうろん",
	"ときめく",
	"とくい",
	"とくぎ",
	"とくせつ",
	"とくてん",
	"とくべつ",
	"とけい",
	"とし",
	"とつぜん",
	"ととのえる",
	"となり",
	"とびら",
	"とぼしい",
	"ともだち",
	"とりえ",
	"とりひき",
	"とりみだす",
	"どうい",
	"どうかん",
	"どうじ",
	"どうとう",
	"どくそう",
	"どくりつ",
	"どようび",
	"どよめく",
	"ないみつ",
	"なおさら",
	"なかなおり",
	"ながい",
	"ながれぼし",
	"なきむし",
	"なぐさめる",
	"なげやり",
	"なだめる",
	"なつ",
	"なみたいてい",
	"なみはずれた",
	"にくらしい",
	"にげまどう",
	"にちようび",
	"にもつ",
	"にんい",
	"にんたい",
	"にんむ",
	"ぬくもり",
	"ねうち",
	"ねがい",
	"ねどこ",
	"ねむる",
	"のうりつ",
	"のぞみ",
	"のはら",
	"のぼせる",
	"のりまわす",
	"はいけい",
	"はいぶん",
	"はえぬき",
	"はかどる",
	"はけん",
	"はなしあい",
	"はる",
	"はんだん",
	"ばんじ",
	"ばんのう",
	"ひかえめ",
	"ひきたてる",
	"ひつよう",
	"ひてい",
	"ひと",
	"ひなん",
	"ひはん",
	"ひやけ",
	"ひらめく",
	"ひろう",
	"ひろば",
	"ひろめる",
	"びか",
	"ふうき",
	"ふうけい",
	"ふかい",
	"ふかのう",
	"ふく",
	"ふこう",
	"ふさわしい",
	"ふしぎ",
	"ふじん",
	"ふつう",
	"ふね",
	"ふゆ",
	"ふんいき",
	"ぶたい",
	"ぶぶん",
	"ぶんか",
	"ぶんけん",
	"ぶんぽう",
	"へや",
	"べんかい",
	"ほうがく",
	"ほうこう",
	"ほうこく",
	"ほうしき",
	"ほうしん",
	"ほうそう",
	"ほうふ",
	"ほかん",
	"ほご",
	"ほたる",
	"ほんい",
	"ほんやく",
	"まいこむ",
	"まごころ",
	"まじめ",
	"まもなく",
	"まもる",
	"まわり",
	"まんぞく",
	"みおとす",
	"みかえり",
	"みかた",
	"みがる",
	"みきわめる",
	"みしらぬ",
	"みじかい",
	"みちがえる",
	"みつめる",
	"みどころ",
	"みならい",
	"みなれない",
	"みのがす",
	"みらい",
	"むいみ",
	"むかし",
	"むかんしん",
	"むきだし",
	"むしばむ",
	"むじつ",
	"むだ",
	"むめい",
	"むやみに",
	"めいあん",
	"めいかい",
	"めいしん",
	"めいぼ",
	"めいもく",
	"めいよ",
	"めいれい",
	"めいろ",
	"めぐりあわせ",
	"めまい",
	"めまぐるしい",
	"もくろく",
	"もしも",
	"もじ",
	"もちこす",
	"ものごと",
	"ものさし",
	"もり",
	"もんだい",
	"やかん",
	"やくひん",
	"やくめ",
	"やくわり",
	"やさい",
	"やさしい",
	"やしなう",
	"やしん",
	"やすらか",
	"やね",
	"やぼう",
	"やま",
	"やりとり",
	"ゆううつ",
	"ゆうかん",
	"ゆうき",
	"ゆうじん",
	"ゆうのう",
	"ゆうれつ",
	"ゆけつ",
	"ゆめ",
	"よいん",
	"ようき",
	"ようご",
	"ようてん",
	"よかん",
	"よくげつ",
	"よくじつ",
	"よけい",
	"よそう",
	"よはく",
	"よびおこす",
	"よほう",
	"よゆう",
	"よる",
	"よわき",
	"らくえん",
	"らくたん",
	"らせん",
	"らんがい",
	"りえき",
	"りかい",
	"りきせつ",
	"りつあん",
	"りてん",
	"りれき",
	"りろん",
	"りんり",
	"るいじ",
	"るいすい",
	"るいせき",
	"れいせい",
	"れいだい",
	"れいとう",
	"れきし",
	"れんさい",
	"れんそう",
	"れんらく",
	"れんりつ",
	"ろうか",
	"ろうどく",
	"ろじ",
	"ろてん",
	"ろんり",
	"わかくさ",
	"わかちあう",
	"わかれ",
	"わきあがる",
	"わきかえる",
	"わくせい",
	"わずかな",
	"わたしから",
	"わたしが",
	"わたしは",
	"わたしも",
	"わたしを",
	"わらい",
	"わりあて"
};
char[] MIKA_kana={
	'あ', /*    1 */
	'い', /*    2 */
	'う', /*    3 */
	'え', /*    4 */
	'お', /*    5 */
	'か', /*    6 */
	'き', /*    7 */
	'く', /*    8 */
	'け', /*    9 */
	'こ', /*   10 */
	'さ', /*   11 */
	'し', /*   12 */
	'す', /*   13 */
	'せ', /*   14 */
	'そ', /*   15 */
	'た', /*   16 */
	'ち', /*   17 */
	'つ', /*   18 */
	'て', /*   19 */
	'と', /*   20 */
	'な', /*   21 */
	'に', /*   22 */
	'ぬ', /*   23 */
	'ね', /*   24 */
	'の', /*   25 */
	'は', /*   26 */
	'ひ', /*   27 */
	'ふ', /*   28 */
	'へ', /*   29 */
	'ほ', /*   30 */
	'ま', /*   31 */
	'み', /*   32 */
	'む', /*   33 */
	'め', /*   34 */
	'も', /*   35 */
	'や', /*   36 */
	'ゆ', /*   37 */
	'よ', /*   38 */
	'ら', /*   39 */
	'り', /*   40 */
	'る', /*   41 */
	'れ', /*   42 */
	'ろ', /*   43 */
	'わ', /*   44 */
	'を', /*   45 */
	'ん', /*   46 */
	'が', /*   47 */
	'ぎ', /*   48 */
	'ぐ', /*   49 */
	'げ', /*   50 */
	'ご', /*   51 */
	'ざ', /*   52 */
	'じ', /*   53 */
	'ず', /*   54 */
	'ぜ', /*   55 */
	'ぞ', /*   56 */
	'だ', /*   57 */
	'ぢ', /*   58 */
	'づ', /*   59 */
	'で', /*   60 */
	'ど', /*   61 */
	'ば', /*   62 */
	'び', /*   63 */
	'ぶ', /*   64 */
	'べ', /*   65 */
	'ぼ', /*   66 */
	'ぱ', /*   67 */
	'ぴ', /*   68 */
	'ぷ', /*   69 */
	'ぺ', /*   70 */
	'ぽ' /*   71 */
	};
String[] MIKA_kana_yomi={
	"a", /*    1 あ */
	"i", /*    2 い */
	"u", /*    3 う */
	"e", /*    4 え */
	"o", /*    5 お */
	"ka", /*    6 か */
	"ki", /*    7 き */
	"ku", /*    8 く */
	"ke", /*    9 け */
	"ko", /*   10 こ */
	"sa", /*   11 さ */
	"si", /*   12 し */
	"su", /*   13 す */
	"se", /*   14 せ */
	"so", /*   15 そ */
	"ta", /*   16 た */
	"ti", /*   17 ち */
	"tu", /*   18 つ */
	"te", /*   19 て */
	"to", /*   20 と */
	"na", /*   21 な */
	"ni", /*   22 に */
	"nu", /*   23 ぬ */
	"ne", /*   24 ね */
	"no", /*   25 の */
	"ha", /*   26 は */
	"hi", /*   27 ひ */
	"hu", /*   28 ふ */
	"he", /*   29 へ */
	"ho", /*   30 ほ */
	"ma", /*   31 ま */
	"mi", /*   32 み */
	"mu", /*   33 む */
	"me", /*   34 め */
	"mo", /*   35 も */
	"ya", /*   36 や */
	"yu", /*   37 ゆ */
	"yo", /*   38 よ */
	"ra", /*   39 ら */
	"ri", /*   40 り */
	"ru", /*   41 る */
	"re", /*   42 れ */
	"ro", /*   43 ろ */
	"wa", /*   44 わ */
	"wo", /*   45 を */
	"nn", /*   46 ん */
	"ga", /*   47 が */
	"gi", /*   48 ぎ */
	"gu", /*   49 ぐ */
	"ge", /*   50 げ */
	"go", /*   51 ご */
	"za", /*   52 ざ */
	"zi", /*   53 じ */
	"zu", /*   54 ず */
	"ze", /*   55 ぜ */
	"zo", /*   56 ぞ */
	"da", /*   57 だ */
	"di", /*   58 ぢ */
	"du", /*   59 づ */
	"de", /*   60 で */
	"do", /*   61 ど */
	"ba", /*   62 ば */
	"bi", /*   63 び */
	"bu", /*   64 ぶ */
	"be", /*   65 べ */
	"bo", /*   66 ぼ */
	"pa", /*   67 ぱ */
	"pi", /*   68 ぴ */
	"pu", /*   69 ぷ */
	"pe", /*   70 ぺ */
	"po" /*   71 ぽ */
	};
String[] MIKA_kana_yomi2={
	null, /*    1 あ */
	null, /*    2 い */
	null, /*    3 う */
	null, /*    4 え */
	null, /*    5 お */
	null, /*    6 か */
	null, /*    7 き */
	null, /*    8 く */
	null, /*    9 け */
	null, /*   10 こ */
	null, /*   11 さ */
	"shi", /*   12 し */
	null, /*   13 す */
	null, /*   14 せ */
	null, /*   15 そ */
	null, /*   16 た */
	"chi", /*   17 ち */
	"tsu", /*   18 つ */
	null, /*   19 て */
	null, /*   20 と */
	null, /*   21 な */
	null, /*   22 に */
	null, /*   23 ぬ */
	null, /*   24 ね */
	null, /*   25 の */
	null, /*   26 は */
	null, /*   27 ひ */
	"fu", /*   28 ふ */
	null, /*   29 へ */
	null, /*   30 ほ */
	null, /*   31 ま */
	null, /*   32 み */
	null, /*   33 む */
	null, /*   34 め */
	null, /*   35 も */
	null, /*   36 や */
	null, /*   37 ゆ */
	null, /*   38 よ */
	"la", /*   39 ら */
	"li", /*   40 り */
	"lu", /*   41 る */
	"le", /*   42 れ */
	"lo", /*   43 ろ */
	null, /*   44 わ */
	null, /*   45 を */
	null, /*   46 ん */
	null, /*   47 が */
	null, /*   48 ぎ */
	null, /*   49 ぐ */
	null, /*   50 げ */
	null, /*   51 ご */
	null, /*   52 ざ */
	"ji", /*   53 じ */
	null, /*   54 ず */
	null, /*   55 ぜ */
	null, /*   56 ぞ */
	null, /*   57 だ */
	null, /*   58 ぢ */
	null, /*   59 づ */
	null, /*   60 で */
	null, /*   61 ど */
	null, /*   62 ば */
	null, /*   63 び */
	null, /*   64 ぶ */
	null, /*   65 べ */
	null, /*   66 ぼ */
	null, /*   67 ぱ */
	null, /*   68 ぴ */
	null, /*   69 ぷ */
	null, /*   70 ぺ */
	null /*   71 ぽ */
	};
	public static void main(String[] args) {
		new MIKATYPE();
	}

	public MIKATYPE() {
		int err;
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    addWindowListener(new WindowAdapter() { /* ウィンドーがクローズされた時の処理 を追加 */
            public synchronized void windowClosing(WindowEvent ev) {
//				System.out.printf("window closed\n");
				try { /* 2023/6/5 追加 */
					MIKA_semaphore.acquire(); /* セマフォー要求 */ /* 2023/6/5 追加 */									
					savekiroku(); /* 練習記録(累積練習時間 最高入力速度 達成日)を保存する */
					MIKA_semaphore.release(); /* セマフォー解放 */ /* 2023/6/5 追加 */
				}						
				catch(InterruptedException e)
				{	 /* 2023/6/5 追加 */
					e.printStackTrace(); /* 2023/6/5 追加 */
				}
				procexit(); /* 成績ファイル書き込み 練習時間記録ファイル書き込み */
				System.exit(0); /* プログラム終了 */
            }
});	
	// リスナー
		MyKeyAdapter myKeyAdapter = new MyKeyAdapter(); 
		addKeyListener(myKeyAdapter);/* キー入力処理追加 */
 		MIKA_s_date=new Date(); /* 練習開始日時取得 */
		MIKA_st_t=System.currentTimeMillis(); /*  練習時間記録ファイル用練習開始時間をミリ秒で取得 */
 		inktable(); /* キーボードの位置テーブル初期化 */
     	File file = new File(MIKA_file_name_seiseki); /* 成績ファイルオープン */
		try {
	         BufferedReader b_reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"Shift-JIS"));
			err=rseiseki(b_reader,MIKA_seiseki); /* 練習成績ファイル読み込み */
			if(err==0) convseiseki(MIKA_seiseki); /* 練習成績ファイルデータ変換 */
			try{
				b_reader.close(); /* 練習成績ファイルクローズ */
			}
			catch ( IOException e) { 
			    	e.printStackTrace();
			}
		}
		catch (UnsupportedEncodingException | FileNotFoundException e) {
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); /* ウィンドー最大サイズ取得 */
		int w = screenSize.width*4/5; /* ウィンドーサイズ 幅を最大幅の4/5に設定 */
	    int h = screenSize.height*4/5; /* ウィンドーサイズ 高さを最大高さの4/5に設定 */
		setSize(w,h); /* ウィンドーサイズを設定 */
//		Toolkit tk = Toolkit.getDefaultToolkit();
//		Image img = tk.getImage( "mikatype.gif" ); /* アイコン画像取得 */
//		setIconImage( img ); /* アイコンの設定 */
		setTitle("美佳タイプ"); /* タイトル設定 */
		setLocationRelativeTo(null); /* 中央に表示 */
		setVisible(true); /* ウィンドー表示 */
	}
	int	rseiseki(BufferedReader b_reader,String[] seiseki) /* 練習成績ファイル読み込み */
	{
			int i,err;
			err=0;
			String seiseki_line;
			for(i=0;i<19;i++) /* 練習成績ファイルを19行読み込み */
			{
				try {
					seiseki_line=b_reader.readLine(); /* 練習成績ファイルを一行読み込み */
//					System.out.printf("seiseki file line=%s\n",seiseki_line);
					if(seiseki_line==null) err=1; /* ファイルエンドの場合はエラーコード 1 設定 */
					else seiseki[i]=seiseki_line; /* 読み込んだ成績を成績テーブルに保存 */
			 	} catch ( IOException e) {
				err=1;
				}
      			if(err!=0) return(err); 
    		}
			return(err);
	}
	void convseiseki(String[] seiseki) /* 成績テーブルから最大速度 達成日 累積練習時間を取得 */
	{
		MIKA_rt_t=seisekitime(seiseki[0]); /* 合計練習時間を取得 */
		MIKA_p_time=seisekitime(seiseki[1]); /* ポジション練習累積練習時間を取得 */
		convseiseki3(seiseki,2,8,MIKA_r_speed,MIKA_r_date,MIKA_r_time); /* ランダム練習 最大速度 達成日 累積練習時間を取得 */
		convseiseki3(seiseki,10,7,MIKA_w_speed,MIKA_w_date,MIKA_w_time); /* 英単語練習 最大速度 達成日 累積練習時間を取得 */
		convseiseki3(seiseki,17,2,MIKA_a_speed,MIKA_a_date,MIKA_a_time); /* ローマ字練習 最大速度 達成日 累積練習時間を取得 */
	}
	long seisekitime(String a) /* 成績ファイルの一行から練習時間を取得 */
	{
			int i;
			long ii;
			String b;
			i=a.length(); /* 成績ファイルの一行の長さを取得 */
			if(i>=13) /* 成績ファイル一行の長さが13以上の場合 */
			{
				b=a.substring(i-13,i); /* 練習時間文字列を取得 */
//			System.out.printf("時間=%s\n",b);
				ii=ttconv(b); /* 練習時間文字列を秒に変換 */
			}
			else ii=0;
			return ii;
	}
	void convseiseki3(String[] seiseki,int i,int ii,double[] r_speed,String[] r_date,long[] r_time)
	// ランダム練習 英単語練習 ローマ字練習の成績を取得 */
	{
		int k;
		int j;
		String a,b,c;
		double speed;
		for(k=0;k<ii;k++)
		{
			j=seiseki[k+i].length(); /* 成績ファイルの一行の長さを取得 */
			if(j>=30)
			{
				c=seiseki[k+i].substring(j-22,j-14); /* 成績ファイルから累積練習時間文字列を取得 */
				b=seiseki[k+i].substring(j-30,j-23); /* 成績ファイルから最高速度文字列を取得 */
//			System.out.printf("ランダム練習 =%s 文字列長=%d 速さ=%s 日付=%s\n",seiseki[k+i],j,b,c);
				speed=Double.parseDouble(b.trim()); /* 最高速度文字列を倍精度実数に変換 */
				r_speed[k]=speed; /* 最高入力速度を保存 */
				r_date[k]=c; /* 達成日を保存 */
				r_time[k]=seisekitime(seiseki[k+i]); /* 累積練習時間を秒に変換して保存 */
			}
		}
	}
	String tconv(long time) /* 練習時間秒を文字列に変換 */
	{
		String a;
		a=t0conv(time,0); /* 練習時間秒を "%5d時間%2d分%2d秒"のフォーマットで文字列に変換 */
		return a;
	}
	String t0conv(long time,int flag) /* 練習時間秒をフォーマットを指定して文字列に変換 */
	{
		String a;
		long t1,t2,t3;
		t3=time%60; /* 秒を計算 */
		time=time/60;
		t2=time%60; /* 分を計算 */
		t1=time/60; /* 時間を計算 */
		if(flag==0)	a=String.format("%5d時間%2d分%2d秒",t1,t2,t3); /* 時分秒を文字列に変換 */
		else a=String.format("%3d時間%2d分%2d秒",t1,t2,t3);
		return a;
	}
long ttconv(String mes) /* 時分秒の文字列を秒に変換 */
	{
		String t1,t2,t3;
		long	i,i1,i2,i3;

//		System.out.printf("練習時間 =%s\n",mes);
		t1=mes.substring(0,5); /* 時間文字列を取得 */
		t2=mes.substring(7,9); /* 分文字列を取得 */
		t3=mes.substring(10,12); /* 秒文字列を取得 */
		i1=Integer.parseInt(t1.trim()); /* 時間文字列を整数に変換 */
		i2=Integer.parseInt(t2.trim()); /* 分文字列を整数に変換 */
		i3=Integer.parseInt(t3.trim()); /* 秒文字列を整数に変換 */
		i=i1*60*60+i2*60+i3; /* 時分秒から秒を算出 */
//		System.out.printf("時間=%s %d 分=%s %d 秒=%s %s\n",t1,i1,t2,i2,t3,i3);
//		System.out.printf("時間=%d 分=%d 秒=%s\n",i1,i2,i3);
		return(i);
	}
	int cfind(char a,String line) /* 文字列から指定の文字の位置を検索する */
	{
		int i;
		int j;
		j=line.length(); /* 文字列長取得 */
		for(i=0;i<1000&&i<j;i++)
		{
			if(a==line.charAt(i)) /* 文字列から指定の文字と一致する文字が見つかった場合 */
			{
				return(i+1);
			}
		}
		return(0); /* 一致する文字が見つからない場合 */
	}
	void inkotable(char a,int a_pos,String[] c_pos,int[] p_x,int[] p_y) /* 文字種に対応した文字のキーの位置を設定する */
	{
		int	i,pos;
		for(i=0;i<4;i++)
		{
			if((pos=cfind(a,c_pos[i]))!=0)
			{
				p_x[a_pos]=i+1; /* =1 最上段 =2 上一段 =3 ホームポジション =4 下一段 */
				p_y[a_pos]=pos; /* 左から数えたキーの位置。最左端は1から始まる */
//			System.out.printf("char %c p_x=%d p_y=%d\n",a,p_x[a_pos],p_y[a_pos]);
			}
		}
	}
	void inktable() /* キーボードの位置テーブル初期化 */
	{
		int	i;
		char a;
		for(i=0x41;i<=0x5a;i++) /* 英大文字に対応したキーの位置を設定 */
		{
			a=(char)i;
			inkotable(a,i-0x41,MIKA_c_post,MIKA_pasciix,MIKA_pasciiy);
		}
		for(i=0x61;i<=0x7a;i++) /* 英小文字に対応したキーの位置の設定 */
		{
			a=(char)i;
			inkotable(a,i-0x61,MIKA_c_post,MIKA_pasciix,MIKA_pasciiy);
		}
		for(i=0x30;i<=0x39;i++) /* 数字に対応したキーの位置の設定 */
		{
			a=(char)i;
			inkotable(a,i-0x30,MIKA_c_post,MIKA_pnumberx,MIKA_pnumbery);
		}
	}
	Dimension keyposit(int x,int y){ /* ポジション練習でキーの位置に対応したキートップの表示位置を仮想座標で求める */
		int x_pos;
		int y_pos;
		Dimension pos;
		x_pos=4*MIKA_width_x+(x-1)*4*MIKA_width_x; /* キートップ左上 x 座標算出 */
		y_pos=4*MIKA_width_y+(y-1)*6*MIKA_width_y+(x-1)*2*MIKA_width_y; /* キートップ左上 y 座標算出 */
		pos=new Dimension(y_pos,x_pos);
		return pos;
	}
	int stringlength(String a) /* 文字列長を半角文字=1 全角文字 =2 で計算する */
	{
		int i,ii,length;
		length=a.length(); /* 文字列長取得 */
		ii=0;
		for(i=0;i<length;i++)
		{
			ii=ii+charlength(a.charAt(i)); /* i番目の文字長を加算 */
		}
		return ii;
	}	
	int charlength(char a) /* 文字が半角文字か全角文字かの判定を行う リターン値 半角=1 全角 =2 */
	{
		int i;
//		System.out.printf("a=%1c code=%d\n",a,(int)a);
		if(a<255) i=1; /* 半角英数の場合 */
		else if(0xff61<=a&&a<=0xff9f) i=1; /* 半角カナ文字の場合 */
		else i=2; /* 半角英数 半角カナ文字以外の場合 */
		return i;
	}
	void cslclr(Graphics g) /* 画面クリア */
	{
		int x,y;
		x=MIKA_win_size.height; /* 画面最大高さ取得 */
		y=MIKA_win_size.width; /* 画面最大幅取得 */
		cslcolor(g,MIKA_bk_color); /* 表示色に背景色を設定 */
		g.fillRect(0,0,y,x); /* 背景色で画面クリア */
	}
	void cslcolor(Graphics g,Color color) /* 表示色を設定 */
	{
		g.setColor(color);
	}
	void cslputscale(Graphics g,int x1,int y1,String a,double scale) /* 仮想座標から実座標に変換して文字列を指定の倍率で表示 */
	{
		Color color1;
		int xx1,yy1;
		int	font_size,font_width,font_hight;
		int ffont_size;
		int i,ii,iii;
		char aa;
	 	Font fg;
		font_size=cslfontsize(scale); /* 文字フォントサイズ取得 */
		ffont_size=(int)(font_size/1.33); /* フォントサイズ補正 */
		font_width=cslfontwidth(1.0); /* 文字表示エリア幅取得 */
		font_hight=cslfonthight(1.0); /* 文字表示エリア高さ取得 */
		fg = new Font("Monospaced" , Font.PLAIN , font_size); /* 文字フォント指定 */
		g.setFont(fg); /* 文字フォント設定 */
		ii=a.length(); /* 表示文字列長取得 */
		iii=0;
		xx1=xcord(x1+MIKA_width_x); /* 表示位置x座標を仮想座標から実座標に変換 */
		for(i=0;i<ii;i++)
		{
			yy1=ycord(y1+MIKA_width_y*iii); /* 表示位置 y座標を仮想座標から実座標に変換 */
			aa=a.charAt(i); /* 文字列からi番目の文字を取り出し */
			g.drawString(String.valueOf(aa),yy1+(font_width-font_size)/2,xx1+(ffont_size-font_hight)/2); /* 表示位置の中央に文字を表示 */
//			if(aa=='ａ') System.out.printf("font_size=%d,font_width%d font hight%d\n",font_size,font_width,font_hight);
//			System.out.printf("x=%d y=%d %s %x \n",yy1,xx1,String.valueOf(aa),(int)aa);
		
			iii=iii+charlength(aa); /* 表示文字位置更新 半角文字は y座標を 1 加算 全角文字は 2加算 */
		}
	}
	char convzenhan(char a) /* 全角文字を半角に変換 */
	{
		if('０'<=a && a<='９') /* 全角の数字の場合 */
		{
			a=(char)(a-'０'+'0'); /* 全角数字を半角数字に変換 */
		}
		else if('Ａ'<=a && a<='Ｚ') /* 全角英大文字を半角英大文字に変換 */
		{
			a=(char)(a-'Ａ'+'A');
		}
		else if('ａ'<=a && a<='ｚ') /* 全角英語小文字を半角英語小文字に変換 */
		{
			a=(char)(a-'ａ'+'a');
		}
		return(a);
	}
	void cslputzscale(Graphics g,int x1,int y1,char a,double scale) /* 半角英数を全角文字に変換して指定の倍率で表示 */
	{
		char aa;
		if('0'<=a&&a<='9') { /* 数字を半角から全角に変換 */
			aa=(char)(a-'0'+'０');
		}
		else if('A'<=a&&a<='Z')
		{ /* 英大文字を半角から全角に変換 */
			aa=(char)(a-'A'+'Ａ');
		}
		else if('a'<=a&&a<='z') { /* 英小文字を半角から全角に変換 */
			aa=(char)(a-'a'+'ａ');
		}
		else if(a==',') /* カンマを半角から全角に変換 */
		{
			aa='，';
		}
		else if(a=='.') /* ピリオドを半角から全角に変換 */
		{
			aa='．';
		}
		else if(a==' ') /* スペースを半角から全角に変換 */
		{
			aa='　';
		}
		else if(a==';') /* セミコロンを半角から全角に変換 */
		{
			aa='；';
		}
		else {
			aa=a;
		}
		cslputscale(g,x1,y1,String.valueOf(aa),scale); /* 文字列を指定で倍率で仮想座標で表示 */
	}
	void cslput(Graphics g,int x,int y,String mes) /* 文字列を仮想座標で表示 */
	{
		cslputscale(g,x,y,mes,1.0); /* 文字列を等倍の倍率で仮想座標で表示 */
	}
	void cslputu(Graphics g,int x,int y,String mes,int yy,Color color1) /* 文字列の下に下線を表示 */
// x 文字列表示左上仮想x座標
// y 文字列表示左上仮想y座標 
// mes アンダーラインを引く文字列
// yy 文字列下端から下線までのドット数間隔の補正値
// color1 表示色 
	{
		int char_length;
		int x1,x2,xx,y1,y2;
		int font_size,ffont_size;
		int font_hight;
		char_length=stringlength(mes); /* 文字列長取得 半角文字は長さ=1 全角文字は長さ=2で計算*/
		font_size=cslfontsize(1.0); /* 等倍のフォントサイズ取得 */
		ffont_size=(int)(font_size/1.33); /* フォントサイズ補正 */
		font_hight=cslfonthight(1.0); /* 表示エリア高さを取得 */
		x1=xcord(x+MIKA_width_x)+yy+(ffont_size-font_hight)/2+2; /* アンダーラインのx座標設定 */
		x2=xcord(1)-xcord(0); /* アンダーラインの太さを設定 */
		y1=ycord(y); /* アンダーラインの開始y座標設定 */
		y2=ycord(y+char_length*8); /* アンダーラインの終了y座標設定 */
		cslcolor(g,color1); /* アンダーラインの色を設定 */
		for(xx=x1;xx<=x1+x2;xx++) /* 指定の太さのアンダーラインを描画 */
		{
			g.drawLine(y1,xx,y2,xx); /* 直線描画 */
		}
	}
void cslmencenter(Graphics g,int x,String mes) /* 中央にメッセージ文字列を表示 */
// x 文字列表示仮想座標
	{
		int y;
		int k;
		int kk;
		if(MIKA_max_y_flag==0) kk=80; /* 横幅半角80文字モード */
		else kk=64; /* 横幅半角64文字モード */
		k=stringlength(mes); /* 文字列長取得  半角文字は長さ=1 全角文字は長さ=2で計算*/
//		System.out.printf("mes=%s lentgh=%s",mes,k);
		y=((kk-k)*MIKA_width_y)/2; /* 表示開始位置計算 */
		cslput(g,x,y,mes); /* 文字列表示 */
	}
	void cslrectb(Graphics g, int x1,int y1,int x2,int y2,Color color1,Color color2,int b) /* ポジション練習のキーを一個表示 */
	{
			cslrectt(g,x1,y1,x2,y2,color1); /* キーの外枠を表示 */
			cslrecttt(g,x1,y1,x2,y2,color2,b); /* キーの内側を塗りつぶし */
	}
	void cslrectt (Graphics g,int x1,int y1,int x2,int y2,Color color) /* 四角を表示 */
	{
		cslrecttt(g,x1,y1,x2,y2,color,0); /* 境界なしで四角を表示 */
	}
	void cslrecttt (Graphics g,int x1,int y1,int x2,int y2,Color color,int b) /* 四角の内側を境界幅bで塗りつぶす */
	{
		int	xx1,xx2,yy1,yy2;
		int bx,by,bb;
		if(b!=0) /* 境界幅が0で無い場合 */
		{
			bx=xcord(b)-xcord(0); /* 縦方向の境界幅を仮想座標から実座標に変換 */
			by=ycord(b)-ycord(0); /* 横方向の境界幅を仮想座標から実座標に変換 */
			bb=Math.min(bx,by); /* 縦方向の境界幅と横方向の境界幅の小さい方の値を設定 */
			if(bb<=0) bb=1; /* 境界幅がゼロ以下の場合は境界幅を位置に設定 */
		}
		else bb=0;
		xx1=xcord(x1)+bb;	/* 左上 x 座標を 仮想座標から実座標に変換 */ 
		xx2=xcord(x2)-bb;	/* 右下 x 座標を 仮想座標から実座標に変換 */
		yy1=ycord(y1)+bb;	/* 左上 y 座標を 仮想座標から実座標に変換 */
		yy2=ycord(y2)-bb;	 /* 右下 y 座標を 仮想座標から実座標に変換 */
		cslcolor(g,color);  /* 表示色を設定 */
		if(xx1<=xx2&&yy1<=yy2)	g.fillRect(yy1,xx1,yy2-yy1,xx2-xx1);	/*四角を描画 */
	}
	void cslellipse(Graphics g,int x1,int y1,int x2,int y2,Color color) /* 指定色で楕円を描画 */
	{
		int	xx1,xx2,yy1,yy2;
		xx1=xcord(x1); /* 左上 x 座標を 仮想座標から実座標に変換 */
		xx2=xcord(x2); /* 右下 x 座標を 仮想座標から実座標に変換 */
		yy1=ycord(y1); /* 左上 y 座標を 仮想座標から実座標に変換 */
		yy2=ycord(y2); /* 右下 y 座標を 仮想座標から実座標に変換 */
		cslcolor(g,color); /* 表示色を設定 */
		g.fillOval(yy1,xx1,yy2-yy1,xx2-xx1); //楕円を描画 */
	}
	void cslkeyback(Graphics g,int x_pos,int y_pos,Color color) /* ポジション練習にてエラー文字とキーガイド文字の背景を塗りつぶす */
	{
		int dx=7;
		int dy=7;
		cslrectt(g,x_pos+MIKA_width_x-dx,y_pos+MIKA_width_y-dy,x_pos+2*MIKA_width_x+dx,y_pos+3*MIKA_width_y+dy,color);
	}
	int cslfonthight(double scale) /* 指定倍率で全角文字の表示エリア高さを取得 */
	{
			int font_hight;
			int font_size;
			font_size=(int)(MIKA_width_x*scale); /* 表示エリア高さを仮想座標で計算 */
			font_hight=xcord(font_size)-xcord(0);  /* 表示エリア高さを実座標に変換 */
			return font_hight;
	}
	int cslfontwidth(double scale) /* 指定倍率で全角文字の表示エリア幅を取得 */
	{
			int font_width;
			int font_size;
			font_size=(int)(MIKA_width_y*2*scale); /* 表示エリア幅を仮想座標で計算 */
			font_width=ycord(font_size)-ycord(0); /* 表示エリア幅を実座標に変換 */
			return font_width;
	}
	int cslfontsize(double scale) /* 指定倍率でフォントサイズを取得 */
	{
		int font_hight;
		int font_width;
		int font_size;
		font_hight=cslfonthight(scale); /* 指定倍率で表示エリア高さを取得 */
		font_width=cslfontwidth(scale); /* 指定倍率で表示エリア幅を取得 */
		font_size=Math.min(font_hight,font_width); /* 表示エリア高さと表示エリア幅の小さい方の値をフォントサイズに指定 */
		return font_size;
	}
	int	xcord(int x1){ /* 仮想x座標を 実x座標に変換 */
		int max_x_cord1;
		int x,xx;
		if(MIKA_max_x_flag==0) /* 縦25行モードの設定 */
		{
			max_x_cord1=25*16;
		}
		else /* 縦20行モードの設定 */
		{
			max_x_cord1=20*16;
		}
		x=MIKA_win_size.height-MIKA_insets.top-MIKA_insets.bottom; /* 有効 x表示高さを計算 */
		xx=(x)*(x1)/max_x_cord1; /* 仮想座標を実座標に変換 */
		xx=xx+MIKA_insets.top; /* 表示位置をウィンドー枠内に補正 */ 
		return(xx);
	}
	int ycord(int y1) /* 仮想y座標を 実y座標に変換 */
	{
		int max_y_cord1;
		int y,yy;
		if(MIKA_max_y_flag==0) /* 一行横 80カラムモードの設定 */
		{
			max_y_cord1=80*8;
		}
		else /* 一行横 64カラムモードの設定 */
		{
			max_y_cord1=64*8;
		}
		y=MIKA_win_size.width-MIKA_insets.left-MIKA_insets.right; /* 有効 y表示幅を計算 */
		yy=(y*y1)/(max_y_cord1); /* 仮想座標を実座標に変換 */
		yy=yy+MIKA_insets.left; /* 表示位置をウィンドー枠内に補正 */
		return(yy);
	}
	int xxcord(int x) /* ランダム練習 英単語練習 ローマ字練習で 練習文字の表示x仮想座標を計算 */
	{
		int xx;
		xx=	MIKA_t_line*16+x*20; /* MIKA_t_lineを開始行にして、ドット高さ20ドットで表示 */
		return xx;
	}
	int yycord(int y) /* ランダム練習 英単語練習 ローマ字練習で 練習文字の表示y仮想座標を計算 */
	{
		int yy;
		yy=y*16; /*  横 16ドット間隔で表示 */
		return yy;
	}
	int homeposi(int x,int y) /* キーの位置がホームポジションかの判定 */
	{ 
		if(x==3&&((1<=y&&y<=4)||(7<=y&&y<=10))) return(1); /* ホームポジションの場合は 1 でリターン */
		else return(0); /* ホームポジション以外は 0でリターン */
	}
	void poofinger(Graphics g,int x_finger,int y_finger,int width_finger,Color color) /* 指の爪を表示 */
	{
		int x1,y1,x2,y2;
		x1=x_finger+4; /* 爪のイラストの左上の x座標取得 */
		y1=y_finger+4; /* 爪のイラストの左上の y座標取得 */
		x2=x_finger+24; /* 爪のイラストの左下の x座標取得 */
		y2=y_finger+width_finger-4; /* 爪のイラストの右上の y座標取得 */
		cslellipse(g,x1-7,y1,x1+7,y2,color); /* 爪の丸みを楕円で表示 */
		cslrectt(g,x1,y1,x2,y2,color); /* 爪の本体の四角を表示 */
	}
	void pofinger(Graphics g,int x_pos,int y_pos,int yubi_haba,int flag)	/* 指を一本表示 */
//	flag=0 指のイラストを描画 
//	flag=1 指のイラストを消去
	{
		Color color;
		int x1,y1,x2,y2;
		if (flag==0) color=MIKA_finger_color; /* 指のイラストを表示する色指定 */
		else color=MIKA_bk_color; /* 指のイラストを消去する色指定 */
		x1=x_pos; /* 指の左上のx座標取得 */
		x2=26*MIKA_width_x;  /* 指の下端のx座標取得 */
		y1=y_pos; /* 指の左上の y座標取得 */
		y2=y_pos+yubi_haba; /* 指の右上の y座標取得 */
		cslellipse(g,x1-8,y1,x1+8,y2,color); /* 指の丸みを楕円で表示 */
		cslrectt(g,x1,y1,x2,y2,color); /* 指の本体を四角で表示 */
		if (flag==0) /* 指のイラストを表示する時には爪のイラストも表示 */
		{
			poofinger(g,x_pos,y_pos,yubi_haba,MIKA_nail_color); /* 爪のイラスト表示 */
		}
	}
	void pfinger(Graphics g,int flag) /* 指のイラスト 10本表示  flag=0 表示 flag=1 消去 */
	{
//	flag=0 指のイラストを描画 
//	flag=1 指のイラストを消去
		int	i;
		for(i=0;i<10;i++) /* 指を10本描く */
		{
			pofinger(g,MIKA_fngpoint[i][0],MIKA_fngpoint[i][1],MIKA_fngpoint[i][2],flag); /* 指を一本づつ表示 */
		}
	}
	int fngposit(int finger) /* キーを打つ指の指定 */
	{
		if(finger==5) finger=4; /* 指を左手人指し指に指定 */
		if(finger==6) finger=7; /* 指を右手人指し指に指定 */
		if(finger==11) finger=10; /* 指を右手小指に指定 */
		if(finger==12) finger=10; /* 指を右手小指に指定 */
		if(finger==13) finger=10; /* 指を右手小指に指定 */
		return finger;
	}
	void difposit(Graphics g,char a,int flag) /* 文字に対応したキーを打つ指の爪を表示 */
// flag=0 爪を黒で表示
// flag=1 爪を元の色に戻して表示
	{
		Dimension pos1;
		Color color1;
		int x,y;
		int	yy;
		int x_finger,y_finger,yubi_haba;
		pos1=keycord(a); /* 文字に対応したキーの位置を取得 */
		x=pos1.height; /* キーの行位置番号を取得 */
		y=pos1.width; /* キーの列位置番号を取得 */
		if(x==0||y==0) return; /* 対応するキーが無い場合は無処理でリターン */
		yy=fngposit(y); /* キー位置に対応した指番号取得 */
		x_finger=MIKA_fngpoint[yy-1][0]; /* 指番号に対応した指の左上 x座標取得 */ 
		y_finger=MIKA_fngpoint[yy-1][1]; /* 指番号に対応した指の左上 y座標取得 */
		yubi_haba=MIKA_fngpoint[yy-1][2]; /* 指番号に対応した指の指幅取得 */

//		System.out.printf("finger x=%d y=%d x_finger=%d y_finger=%d yubi_haba=%d\n",x,y,x_finger,y_finger,yubi_haba);
		if(flag==0) /* フラグが=0の時は指の爪を黒で表示 */
		{
			color1=Color.black;
		}
		else /* フラグが=1の時は指の爪を元の色に戻して表示 */
		{
			color1=MIKA_nail_color;
		}
		poofinger(g,x_finger,y_finger,yubi_haba,color1); /* 指の爪を表示 */
	}
	void 	dispguidechar(Graphics g,char key_char,int flag) /* ポジション練習で練習文字を表示 */
// flag=0 練習文字を黒色で表示
// flag=1 練習文字を消去
	{
		Color	color;
		if(key_char!=0) /* 練習文字がゼロでない場合 */
		{
				if(flag==0) color=Color.blue; /* フラグがゼロの時は青色で表示 */
				else color=MIKA_bk_color; /* フラグが1の時は表示を消去 */
				cslcolor(g,color); /* 表示色を設定 */
				cslputzscale(g,2*MIKA_width_x-2,34*MIKA_width_y+1,key_char,3.0); /* 指定位置に 三倍の大きさで練習文字を表示 */
		}
	}
	void dipline(Graphics g,int x, String line,int flag) /* キーボード一列表示*/
// x 表示行位置番号 
// line キートップ文字列 
// flag=0 キートップとキーの刻印文字を表示 
// flag=1 キートップのみ表示してキーの刻印は表示しない
// flag=2 キーの刻印のみを表示
// flag=3 キーの刻印を消去
	{
		int x_pos;
		int y_pos;
		int y,yy;
		int x1,x2,y1,y2;
		int x3,y3;
		Dimension pos;
		Color color1,color2,color3;
		yy=line.length(); /* キートップ文字列長取得 */
		for(y=0;y<yy&&y<14;y++)
		{	
			pos=keyposit(x+1,y+1); /* キーの表示位置の仮想座標を取得 */
			x_pos=pos.height;
			y_pos=pos.width;
			x1=x_pos; /* 表示開始 x 座標取得 */
			y1=y_pos-4; /* 表示開始 y座標取得 */
			x2=x_pos+3*MIKA_width_x; /* 表示終了 x 座標取得 */
			y2=y_pos+4*MIKA_width_y+4; /* 表示終了 y 座標取得 */
			x3=x_pos+MIKA_width_x; /* 表示 文字位置 x 座標取得 */
			y3=y_pos+MIKA_width_y; /* 表示 文字位置 y 座標取得 */
//			System.out.printf("x_pos=%d y_pos=%d\n",x_pos,y_pos);
			
			if (homeposi(x+1,y+1) == 1) /* ホームポジションはマゼンタで表示 */
			{
					color1=Color.black; /* キー外枠は黒色 */
					color2=Color.magenta; /* キー内側はマゼンタ */
					color3=Color.black; /* 文字は黒色 */
			}
			else /* ホームポジション以外はグレーで表示 */
			{
					color1=Color.black; /* キー外枠は黒色 */
					color2=Color.gray; /* キー内側はグレー */
					color3=Color.black; /* 文字は黒色 */
			}
			if(flag==0||flag==1) /* キーの背景を表示 */
			{
				cslrectb(g,x1,y1,x2,y2,color1,color2,1); /* 外枠付きでキーを表示 */
			}
			if(flag==0||flag==2) /* キーの刻印文字を表示 */
			{
				cslcolor(g,color3); /* キー刻印の表示色を指定 */
				cslputzscale(g,x3,y3,line.charAt(y),1.8); /* キーの刻印を 1.8倍で表示 */
			}
			else if (flag==3) cslkeyback(g,x_pos,y_pos,color2); /* キーの刻印を消去 */
		}
	}
	int dikposit(Graphics g,char a,int flag) /* ポジション練習でエラー文字とガイドキー文字の表示及び復帰を行う */
//	a ガイドキーの文字
//	flag=0 ガイドキー文字を黒い背景で表示
//	flag=1 ガイドキー文字の表示をキーの刻印ありで復帰
//	flag=2 ガイドキー文字の表示をキーの刻印なしで復帰
//	flag=3 エラーキー文字の表示は赤い背景で表示
	{
		int	x;
		int	y;
		int x_posit;
		int y_posit;
		Color BkColor,TextColor;
		Dimension pos1,pos2;
		if(a==0) return(0);
		pos1=keycord(a); /* 文字からキーの位置を算出 */
		x=pos1.height; /* キー位置の行番号を取得 */
		y=pos1.width; /* キー位置の列番号を取得 */
		if(x==0||y==0) return(0);
		pos2=keyposit(x,y); /* キー位置に対応した仮想座標を取得 */
		x_posit=pos2.height;
		y_posit=pos2.width;
		if(flag==0) /* ガイドキー文字表示の場合 */
		{
			if (homeposi(x,y)==1) /* ガイドキー文字がホームポジョションの場合 */
			{
				BkColor=Color.black; /* 背景は黒で表示 */
				TextColor=Color.magenta; /* 文字はマゼンタで表示 */
			}
			else /* ホームポジションではない場合 */
			{
				BkColor=Color.black; /* 背景は黒で表示 */
				TextColor=Color.gray; /* 文字はグレーで表示 */
			}
		}
		else if(flag==1||flag==2) /* 表示復帰の場合 */
		{
			if (homeposi(x,y)==1) /* ガイドキー文字がホームポジションの場合 */
			{
					BkColor=Color.magenta; /* 背景はマゼンタで表示 */
					TextColor=Color.black; /* 文字は黒で表示 */
			}
			else
			{
					BkColor=Color.gray; /* 背景はグレーで表示 */
					TextColor=Color.black; /* 文字は黒で表示 */
			}
		}
		else /* flag==3 エラーキー表示の場合 */
		{
			BkColor=MIKA_color_position_err; /* 背景は赤で表示 */
			TextColor=Color.black; /* 文字は黒で表示 */
		}
		cslkeyback(g,x_posit,y_posit,BkColor); /* 背景を表示 */
		cslcolor(g,TextColor); /* 表示色を文字色に設定 */
		if(flag==0||flag==1||flag==3)
		{
			cslputzscale(g,x_posit+MIKA_width_x,y_posit+MIKA_width_y,a,1.8); /* 文字を1.8倍の倍率で表示 */
		}
		return(0);
	}
	void diposit(Graphics g,int flag)
// flag=0 キートップとキーの刻印文字を表示 
// flag=1 キートップのみ表示してキーの刻印は表示しない
// flag=2 キーの刻印のみを表示
// flag=3 キーの刻印を消去 
	{
		dipline(g,0,MIKA_c_pos1,flag); /* 最上段 キーボード表示 */
		dipline(g,1,MIKA_c_pos2,flag); /* 上一段 キーボード表示 */
		dipline(g,2,MIKA_c_pos3,flag); /* ホームポジション キーボード表示 */
 		dipline(g,3,MIKA_c_pos4,flag); /* 下一段 キーボード表示 */
	}
	void disperrorcount(Graphics g,int flag,int i,int j) /* エラー入力回数表示 */
// flag=0 表示 flag=1 数値のみ消去 flag=2 メッセージと共に数値を消去
// i 表示位置縦行番号
// j 表示位置横列番号
	{
		String type_mes;
		int offset;
		if(flag==0) /* フラグが=0の時は表示色を赤色に設定 */
		{
 			cslcolor(g,MIKA_red);
			type_mes=String.format("ミスタッチ%3d回",MIKA_type_err_count); /* エラーカウントメッセージ作成 */
			offset=0;
		}
		else if(flag==1)
		{
			cslcolor(g,MIKA_bk_color); /* フラグが=1の時は表示を消去 */
			type_mes=String.format("%3d",MIKA_type_err_count); /* エラーカウントメッセージ作成 */
			offset=10;
		}
		else
		{
			cslcolor(g,MIKA_bk_color); /* フラグが=1の時は表示を消去 */
			type_mes=String.format("ミスタッチ%3d回",MIKA_type_err_count); /* エラーカウントメッセージ作成 */
			offset=0;
		}
		//		System.out.printf("i=%d j=%d",i,j);
		cslput(g,i*16,(j+offset)*8,type_mes); /* 指定位置にエラーカウント表示 */
	}
	
	void disperror(Graphics g,int flag) /* ポジション練習エラー回数表示 */
// flag=0 表示 flag=1 消去  flag=2 メッセージと共に数値を消去
	{
		disperrorcount(g,flag,3,64); /* 表示位置を指定してエラー回数表示 */
	}
	void disperror1(Graphics g,int flag) /* ランダム練習 英単語演習 エラー回数表示 */
// flag=0 表示 flag=1 消去  flag=2 メッセージと共に数値を消去
	{
		disperrorcount(g,flag,5,49); /* 表示位置を指定してエラー回数表示 */
	}
	void disperror2(Graphics g,int flag) /* ローマ字練習 エラー回数表示 */
// flag=0 表示 flag=1 消去  flag=2 メッセージと共に数値を消去
	{
		disperrorcount(g,flag,5,65);/* 表示位置を指定してエラー回数表示 */
	}
	void dispseikai(Graphics g,int flag) /* 正解数表示 */
// flag=0 表示 flag=1 数値のみ消去 flag=2 メッセージと共に数値を消去
	{
		String type_mes;
		int offset;
		if(MIKA_type_count==0) return;
		if(flag==0)
		{
			cslcolor(g,MIKA_cyan); /* フラグが0の時は表示色をシアンに設定 */
			type_mes=String.format("正解%3d回",MIKA_type_count); /* 正解数メッセージ作成 */
			offset=0;
		}
		else if(flag==1)
		{
			cslcolor(g,MIKA_bk_color); /* フラグが1の時は表示を消去 */
			type_mes=String.format("%3d",MIKA_type_count); /* 正解数メッセージ作成 */
			offset=4;
		}
		else
		{
			cslcolor(g,MIKA_bk_color); /* フラグが1の時は表示を消去 */
			type_mes=String.format("正解%3d回",MIKA_type_count); /* 正解数メッセージ作成 */
			offset=0;
		}
		cslput(g,2*16,(64+offset)*8,type_mes); /* 正解数メッセージ表示 */
	}
	void dispkeygideonoff(Graphics g,int flag) /* キーガイド表示オンオフメッセージ表示 */
// flag=1 前回のメッセージを消去してから今回のメッセージを表示
// flag=0 今回のメッセージのみを表示
	{
		String keymes1,keymes2;
		if(MIKA_menu_kind_flag==MIKA_key_guide_on) /* ガイドキー文字表示がオンの場合 */
		{
			keymes1=MIKA_keymes1; /* 表示メッセージに「スペースを押すとキーガイドを消去します」を指定 */
			keymes2=MIKA_keymes2; /* 消去メッセージに「スペースを押すとキーガイドを表示します」を指定 */
		}
		else /* ガイドキー文字表示がオフの場合 */
		{
			keymes1=MIKA_keymes2; /* 表示メッセージに「スペースを押すとキーガイドを表示します」を指定 */
			keymes2=MIKA_keymes1; /* 消去メッセージに「スペースを押すとキーガイドを消去します」を指定 */
		}
		if(flag==1)
		{
			cslcolor(g,MIKA_bk_color); /* メッセージの表示色を背景色に設定 */
			cslput(g,16,1,keymes2); /* 前回のメッセージを消去 */
		}
		cslcolor(g,MIKA_cyan); /* メッセージの表示色をシアンに設定 */
		cslput(g,16,1,keymes1); /* 今回のメッセージを表示 */
	}
	void disptitle(Graphics g,String mes,String submes) /* 練習項目を画面上部に表示 */
// mes 練習種別メッセージ
// submes 練習項目メッセージ
	{
		String mes0;
		mes0=String.format(mes,submes); /* 表示メッセージを作成 */
		cslcolor(g,MIKA_magenta); /* 表示色をマゼンタに設定 */
		cslmencenter(g,1,mes0); /* 画面上部中央にメッセージを表示 */
//		System.out.printf(mes0);
	}
	void dispkaisumes(Graphics g,int flag,int i,int j) /* 練習回数表示 */
// flag=0 表示 flag=1 消去 
// i 表示位置縦行番号
// j 表示位置横列番号
	{
		String type_mes;
		int count;
		if(MIKA_p_count==null) return; /* 練習回数配列アドレスが空の時はリターン */
		count=MIKA_p_count[MIKA_type_kind_no]; /* 練習項目に対応する練習回数取り出し */
//		System.out.printf("count=%d  MIKA_type_kind_no=%d\n",count,MIKA_type_kind_no);
		if(count==0) return; /* 練習回数がゼロの時はリターン */
		if(flag==0) cslcolor(g,MIKA_green); /* フラグが=0の時は表示色を緑色に設定 */
		else cslcolor(g,MIKA_bk_color); /* フラグが=1の時は表示を消去 */
		type_mes=String.format("練習回数%4d回",count); /* 練習回数メッセージ作成 */
		cslput(g,i*16,j*8,type_mes); /* 練習回数メッセージ表示 */
	}
	void dispkaisu(Graphics g,int flag) /* ポジション練習 練習回数表示 */
// flag=0 表示 flag=1 消去 
	{
		dispkaisumes(g,flag,1,64);
	}
	void dispkaisu2(Graphics g,int flag) /* ランダム練習 英単語練習 ローマ字練習 練習回数表示 */
// flag=0 表示 flag=1 消去 
	{
		dispkaisumes(g,flag,1,31);
	}
	void dispabortmessage(Graphics g,int flag,int i,int j) /* 「ESCキーを押すと中断します」のメッセージを表示 */
// flag=0 表示 flag=1 消去 
// i 表示位置縦行番号
// j 表示位置横列番号
	{
		Color color1;
		int ii,jj;
		if(flag==0) cslcolor(g,MIKA_cyan);  /* フラグが=0の時は表示色をシアンに設定 */
		else cslcolor(g,MIKA_bk_color); /* フラグが=1の時は表示を消去 */
		ii=i*16;
		jj=j*8;
		if(jj<=0) jj=1;
		cslput(g,ii,jj,MIKA_abort_mes);	/* 「ESCキーを押すと中断します」のメッセージを表示 */
	}
	void dispabortmes(Graphics g,int flag) /* ランダム練習 英単語練習で 「ESCキーを押すと中断します」のメッセージを表示 */
// flag=0 表示 flag=1 消去 
	{
		dispabortmessage(g,flag,2,0);
	}
	void dispabortmes2(Graphics g,int flag) /* ローマ字練習 英単語練習で 「ESCキーを押すと中断します」のメッセージを表示 */
// flag=0 表示 flag=1 消去 
	{
		dispabortmessage(g,flag,23,20);
	}
	void dispsecond(Graphics g,int flag) /* ポジション練習で練習時間秒を表示 */
// flag=0 表示 flag=1 消去 
	{
		String	type_mes;
		if(flag==0) cslcolor(g,MIKA_blue);  /* フラグが=0の時は表示色を青に設定 */
		else cslcolor(g,MIKA_bk_color); /* フラグが=1の時は表示を消去 */
		type_mes=String.format("今回は  %4.0f秒かかりました",MIKA_type_speed_time); /* 表示メッセージ作成 */
		cslput(g,2*16,1,type_mes); /* 練習時間秒のメッセージを表示 */
	}
	void dispkeyguidonoffmes(Graphics g,int flag)
//	「この次は、スペースキーを押してキーガイドの表示を消して練習してみましょうね」あるいは
//	「この次は、スペースキーを押してキーガイドを表示して練習してみましょうね」のメッセージを表示
// flag=0 表示 flag=1 消去 
	{
		if(flag==0) cslcolor(g,MIKA_green);  /* フラグが=0の時は表示色を緑に設定 */
		else cslcolor(g,MIKA_bk_color); /* フラグが=1の時は表示を消去 */
		if(MIKA_key_guide_flag==1) /* キーガイドメッセージ表示フラグが 1の場合 */
		cslput(g,20*16,2*8,MIKA_keymes3); /*「この次は、スペースキーを押してキーガイドの表示を消して練習してみましょうね」 のメッセージを表示 */
		else /* キーガイドメッセージ表示フラグが 2の場合 */
		cslput(g,20*16,2*8,MIKA_keymes4); /*「この次は、スペースキーを押してキーガイドを表示して練習してみましょうね」 のメッセージを表示 */
	}
	void dispptrain(Graphics g,String mestb) /* ポジション練習実行画面を表示 */
	{
		cslclr(g); /* 画面クリア */
		disptitle(g,mestb,MIKA_type_kind_mes); /* 練習項目を表示 */
		if (MIKA_p_count[MIKA_type_kind_no]!=0) /* 練習回数がゼロでない場合 */
		{
			dispkaisu(g,0); /* 練習回数を表示 */
		}
		dispkeygideonoff(g,0); /* スペースキーを押すとキーガイドを消去しますのメッセージを表示 */
		if(MIKA_practice_end_flag==0) /* 練習実行中の場合 */
		{
			dispabortmes(g,0); /* エスケープキーを押すと中断しますのメッセージを表示 */
		}
		cslcolor(g,MIKA_cyan); /* 表示色をシアンに設定 */
		cslput(g,2*16,38*8,MIKA_key_type_mes); /* のキーを打ちましょうねのメッセージを表示 */
		cslcolor(g,Color.black); /* 表示色を黒に設定 */
		dispguidechar(g,MIKA_key_char,0);	/* 練習文字を表示 */
		if(MIKA_type_count>0) /* 正解の入力数がゼロより多い場合 */
		{
			dispseikai(g,0); /* 正解の回数を表示 */
		}
		if(MIKA_type_err_count>0) /* エラーの入力数がゼロより多い場合 */
		{
			disperror(g,0); /* エラーの回数を表示 */
		}
		if(MIKA_menu_kind_flag==MIKA_key_guide_on)	diposit(g,0); /* キーガイドが表示ありの場合はキーとキーの文字の刻印を表示 */
		else diposit(g,1); /* キーガイドが表示無しの場合はキーの表示だけでキーの文字の刻印を表示しない */
		dikposit(g,MIKA_err_char,3); /* エラー文字を赤色表示 */
		dikposit(g,MIKA_guide_char,0); /* MIKA_guide_charがゼロでないときキーボードのガイドキーを表示 */
		if(MIKA_practice_end_flag==0) /* 練習実行中の場合 */
		{
			pfinger(g,0); /* 指のイラストを表示 */
			difposit(g,MIKA_guide_char,0); /* MIKA_guide_charがゼロでないとき、使用する指の指示を表示 */
		}
		else /* 練習を終了した場合 */
		{
			dispretrymessage(g,0); /* リトライメッセージ表示 */
			if(MIKA_type_end_flag==1) /* 60文字入力して練習が終了した場合 */
			{
				dispsecond(g,0);	/* 練習時間秒を表示 */
			}
			if(MIKA_key_guide_flag!=0) /* キーガイドメッセージ表示フラグがゼロでない場合 */
			{
				dispkeyguidonoffmes(g,0); /* 次回はスペースキーを押してキーガイドを消去あるいは表示して練習しましょうの表示をする */
			}
		}
	}
	void dispctable(Graphics g) /* ランダム練習 英単語練習 ローマ字練習 練習テキスト表示 */
	{
		char a;
		int i,j,k;
		int ii,jj;
		int kazu_yoko=40; /* 横文字数 */
		k=0;
		for(j=0;j<MIKA_cline_x;j++) /* 練習行数まで表示 */
		{
			for(i=0;i<kazu_yoko;i++) /* 横一行40文字表示 */
			{
				if(k>=MIKA_cline_c) break; /* 練習文字数まで表示 */
				a=MIKA_chat_t[j][i]; /* 練習文字を取得 */
				jj=xxcord(j); /* 練習文字の縦位置を仮想座標に変換 */ /* 2023/2/25 追加 */
				ii=yycord(i); /* 練習文字の横位置を仮想座標に変換 */ /* 2023/2/25 追加 */
				if(MIKA_err_char_flag==1&&j==MIKA_c_p2&&i==MIKA_c_p1) /* 練習文字がエラー文字の場合 */
				{
					cslcolor(g,Color.red); /* 表示色を赤に設定 */
//					dispbkchar(g,j,i); /* 文字の背景を赤色で表示 */ /* 2023/2/25 旧コード */
					dispbkchar(g,jj,ii,MIKA_random_scale); /* 文字の背景を赤色で表示 */ /* 2023/2/25 新コード */
				}
				cslcolor(g,Color.black); /* 表示色を黒に設定 */
//				jj=xxcord(j); /* 練習文字の縦位置を仮想座標に変換 */ /* 2023/2/25 削除 */
//				ii=yycord(i); /* 練習文字の横位置を仮想座標に変換 */ /* 2023/2/25 削除 */
//				cslputzscale(g,jj,ii,a,1.0); /* 指定の位置に文字を表示 */ /* 2023/2/25 旧コード */
				cslputzscale(g,jj,ii,a,MIKA_random_scale); /* 指定の位置に文字を表示 */ /* 2023/2/25 新コード */
				if(j<MIKA_c_p2||(j==MIKA_c_p2&&i<MIKA_c_p1)) /* 入力済の文字には下線を引く */
				{
					cslputu(g,jj,ii,"aa",1,MIKA_color_text_under_line);	
				}
				k++; /* 表示文字数インクリメント */
			}
		}
	}
	void dispmaxspeedrecord(Graphics g,int i1,int j1,int i2,int j2) /* ランダム練習 英単語練習 ローマ字練習の 最高入力速度と 達成日を表示 */
	{
			String a,b;
			cslcolor(g,MIKA_green); /* 表示色を緑に設定 */
			a=String.format("最高入力速度%6.1f文字／分",MIKA_type_speed_record[MIKA_type_kind_no]); /* 最高速度メッセージ作成 */
			cslput(g,i1*16,j1*8,a); /* 最高速度メッセージ表示 */
			b=String.format("達成日 %s",MIKA_type_date_record[MIKA_type_kind_no]); /* 達成日メッセージ作成 */
			cslput(g,i2*16,j2*8,b); /* 達成日メッセージ表示 */
	}
	void disptrain(Graphics g,String mest) /* ランダム練習 英単語練習 実行画面の表示 */
	{
		String a,b;
		cslclr(g); /* 画面クリア */
		disptitle(g,mest,MIKA_type_kind_mes); /* 練習項目を表示 */
		cslcolor(g,MIKA_green); /* 表示色を緑に設定 */
		cslput(g,4*16,4*8,"制限時間60秒"); /* 制限時間を表示 */
		if (MIKA_p_count[MIKA_type_kind_no]!=0) /* 練習回数がゼロでない場合 */
		{
			dispkaisu2(g,0); /* 練習回数を表示 */
		}
		if (MIKA_type_speed!=0.0) /* 入力速度がゼロでない場合 */
		{
			dispspeedrate(g,0); /* 入力速度を表示 */
		}
		if (MIKA_type_speed_time!=0.0) /* 経過秒がゼロでない場合 */
		{
				disptime(g,0); /* 経過秒表示 */
		}
		if (MIKA_type_err_count!=0) /* エラー回数がゼロで無い場合 */
		{
			disperror1(g,0); /* エラー回数表示 */
		}
		if(MIKA_type_speed_record[MIKA_type_kind_no]!=0.0) /* 最高入力速度がゼロでない場合 */
		{
			dispmaxspeedrecord(g,3,20,3,49); /* 最高入力速度と達成日時を表示 */
		}
		dispctable(g); /* 練習文字を表示 */
		if(MIKA_practice_end_flag==1) /* 練習終了時 */
		{
			if(MIKA_type_syuryou_flag==2) /* 記録更新時 */
			{
				dispupmes(g); /* 記録を更新しましたの表示を行う */
			}
			dispretrymessage(g,0); /* リトライメッセージ表示 */
		}
		else
		{
			dispabortmes2(g,0); /* エスケープキーを押すと中断しますのメッセージを表示 */
		}
	}
	void dispatrain(Graphics g,String mest) /* ローマ字ランダム練習 ローマ字単語練習 実行画面の表示 */
	{
		cslclr(g); /* 画面クリア */
		disptitle(g,mest,MIKA_type_kind_mes); /* 練習項目を表示 */
		cslcolor(g,MIKA_blue); /* 表示色を青に設定 */
		cslput(g,2*16,28*8,"ローマ字＝"); 
		cslcolor(g,MIKA_green); /* 表示色を緑に設定 */
		cslput(g,4*16,4*8,"制限時間60秒"); /* 制限時間を表示 */
		if (MIKA_p_count[MIKA_type_kind_no]!=0) /* 練習回数がゼロでない場合 */
		{
			dispkaisu2(g,0); /* 練習回数を表示 */
		}
		if (MIKA_type_speed!=0.0) /* 入力速度がゼロでない場合 */
		{
			dispspeedrate2(g,0); /* 入力速度を表示 */
		}
		if (MIKA_type_speed_time!=0.0) /* 経過秒がゼロでない場合 */
		{
			disptime(g,0); /* 経過秒表示 */
		}
		if (MIKA_type_err_count!=0) /* エラー回数がゼロで無い場合 */
		{
			disperror2(g,0); /* エラー回数表示 */
		}
		if(MIKA_type_speed_record[MIKA_type_kind_no]!=0.0) /* 最高入力速度がゼロでない場合 */
		{
			dispmaxspeedrecord(g,4,38,4,65); /* 最高入力速度と達成日時を表示 */
		}
		dispromaji(g,MIKA_romaji,0); /* ローマ字表記ガイドを表示 */
		dispctable(g); /* 練習ひらがなを表示 */
		if(MIKA_practice_end_flag==1) /* 練習終了時 */
		{
			if(MIKA_type_syuryou_flag==2) /* 記録更新時 */
			{
				dispupmes(g);  /* 記録を更新しましたの表示を行う */
			}
			dispretrymessage(g,0); /* リトライメッセージ表示 */
		}
		else
		{
			dispabortmes2(g,0); /* エスケープキーを押すと中断しますのメッセージを表示 */
		}
}
	void ppseiseki(Graphics g,int i,int j,String[] menu_mes,double[] r_speed,String[] r_date,long[] r_time) /* 成績表示 ランダム練習、英単語練習、ローマ字練習表示 */
/* i 表示位置 j 表示個数 menu_mes 練習項目 r_speed 最高速度 r_date 達成日 r_time 累積練習時間 */
	{
		int ii;
		String a,b;
		for(ii=0;ii<j;ii++)
		{
			cslput(g,(i+ii)*16,1,menu_mes[ii]); /* 練習項目を表示 */
			if(r_speed[ii]!=0.0) /*最高入力速度が 0.0 でない場合 */
			{
				a=String.format("%6.1f",r_speed[ii]); /* 最高入力速度を文字列に変換 */
				cslput(g,(i+ii)*16,33*8,a); /* 最高入力速度を表示 */
			}
			cslput(g,(i+ii)*16,44*8,r_date[ii]); /* 達成日を表示 */
			b=tconv(r_time[ii]); /* 累積練習時間を文字列に変換 */
			cslput(g,(i+ii)*16,54*8,b); /* 累積練習時間を表示 */
		}
}
	void dispseiseki(Graphics g) /* 成績表示 */
	{
		int i;
		long time_i;
		String a,aa,b;
		cslclr(g); /* 画面クリア */
		a=tconv(MIKA_rt_t); /* 前回までの合計練習時間を文字列に変換 */
		aa=String.format("前回までの練習時間　%s",a); /* 前回までの合計練習時間のメッセージ作成 */
		cslcolor(g,MIKA_green); /* 表示色を緑色に設定 */
		cslput(g,1,1,aa); /* 前回までの合計練習時間を表示 */
		cslcolor(g,MIKA_blue); /* 表示色を青色に設定 */
		cslput(g,1,43*8,MIKA_return_mes); /* エスケープキーを押すとメニューに戻りますのメッセージを表示 */
		MIKA_lt_t=System.currentTimeMillis(); /* 現在時刻をミリ秒で取得 */
		time_i=timeinterval(MIKA_st_t,MIKA_lt_t); /* 今回練習時間を秒で計算 */
		a=tconv(time_i); /* 今回練習時間を文字列に変換 */
		aa=String.format("今回の練習時間　　　%s",a); /* 今回練習時間のメッセージを作成 */
		cslcolor(g,MIKA_green); /* 表示色を緑色に設定 */
		cslput(g,16,1,aa); /* 今回練習時間を表示 */
		cslcolor(g,MIKA_blue); /* 表示色を青色に設定 */
		cslput(g,3*16,1,MIKA_mest2); /* 表示項目の表題を表示 */
		cslcolor(g,MIKA_orange); /* 表示色をオレンジに設定 */
		b=tconv(MIKA_p_time); /* ポジション練習の累積練習時間を文字列に変換 */
		cslput(g,4*16,54*8,b); /* ポジション練習の累積練習時間を表示 */
		cslput(g,4*16,1,MIKA_menu_mes_s[0]); /* 練習項目「ポジション練習」を表示 */
		ppseiseki(g,6,8,MIKA_menu_mes,MIKA_r_speed,MIKA_r_date,MIKA_r_time); /* ランダム練習の成績を表示 */
		ppseiseki(g,15,7,MIKA_menu_mes_w,MIKA_w_speed,MIKA_w_date,MIKA_w_time); /* 英単語練習の成績を表示 */
		ppseiseki(g,23,2,MIKA_menu_mes_r,MIKA_a_speed,MIKA_a_date,MIKA_a_time); /* ローマ字練習の成績を表示 */
	}
	void dispstart(Graphics g) /* 著作権表示 */
	{
		int i;
		MIKA_max_x_flag=1; /* 縦 20行モードに設定 */
		MIKA_max_y_flag=1;/* 横 64カラムモードに設定 */
		String title_bar="●●●●●●●●●●●●●●●●●●●●●●●●●";
		cslclr(g); /* 画面クリア */
		cslcolor(g,MIKA_magenta); /* 表示色をマゼンタに設定 */
		cslput(g,3*16,7*8,title_bar); /* 表示枠 上端を表示 */
		for (i=4;i<15;i++)
		{
			cslput(g,i*16,7*8,"●"); /* 表示枠 左端を表示 */
			cslput(g,i*16,55*8,"●"); /* 表示枠 右端を表示 */
		}	
		cslput(g,15*16,7*8,title_bar); /* 表示枠 下端を表示 */
		cslcolor(g,MIKA_blue); /* 表示色を青に設定 */
		cslmencenter(g,5*16+8,"美佳のタイプトレーナー");
		cslcolor(g,MIKA_cyan); /* 表示色をシアンに設定 */
		cslmencenter(g,7*16+8,"ＭＩＫＡＴＹＰＥ Ｖer２.０６.０３");
		cslcolor(g,MIKA_orange); /* 表示色をオレンジに設定 */
		cslmencenter(g,9*16+6,"＜＜より高速なタイピングのために＞＞");
		cslmencenter(g,11*16+4,"めざせ一分間２００文字入力");
		cslcolor(g,MIKA_cyan); /* 表示色をシアンに設定 */
		cslmencenter(g,14*16-8,"Copy right 1991/10/12  今村 二朗");
		cslput(g,17*16,24*8,"キーをどれか押すとメニューを表示します");
		MIKA_max_x_flag=0; /* 縦 25行モードに戻す */
		MIKA_max_y_flag=0; /* 横 80カラムモードに戻す */
	}
	void dispmen(Graphics g) /* メニュー及び練習画面表示 */
	{
		if(MIKA_exec_func_no==0) dispstart(g); /* 著作権表示 */
		else if (MIKA_exec_func_no==1) menexe(g,MIKA_menu_mes_s,MIKA_menu_cord_s,MIKA_menu_s_function,MIKA_menu_s_sel_flag,MIKA_mes0); /* 初期メニュー表示 */
		else if (MIKA_exec_func_no==21) menexe(g,MIKA_menu_mes,MIKA_menu_cord,MIKA_position_menu_function,MIKA_position_sel_flag,MIKA_mes0a); /* ポジション練習メニュー表示 */
		else if (MIKA_exec_func_no==22) menexe(g,MIKA_menu_mes,MIKA_menu_cord,MIKA_random_menu_function,MIKA_random_sel_flag,MIKA_mes0b); /* ランダム練習メニュー表示 */
		else if (MIKA_exec_func_no==23) menexe(g,MIKA_menu_mes_w,MIKA_menu_cord,MIKA_word_menu_function,MIKA_word_sel_flag,MIKA_mes0c); /* 英単語練習メニュー表示 */
		else if (MIKA_exec_func_no==24) menexe(g,MIKA_menu_mes_r,MIKA_menu_cord,MIKA_romaji_menu_function,MIKA_romaji_sel_flag,MIKA_mes0d);	 /* ローマ字練習メニュー表示 */
		else if (MIKA_exec_func_no==29) dispseiseki(g); /* 成績表示 */
	else if(MIKA_exec_func_no>400&&MIKA_exec_func_no<500) dispptrain(g,MIKA_mestb); /* ポジション練習の各項目の実行画面表示 */
	else if(MIKA_exec_func_no>500&&MIKA_exec_func_no<600) disptrain(g,MIKA_mestc); /* ランダム練習の各項目の実行画面表示 */
	else if(MIKA_exec_func_no>600&&MIKA_exec_func_no<700) disptrain(g,MIKA_mesta); /* 英単語練習の各項目の実行画面表示 */
	else if(MIKA_exec_func_no>700&&MIKA_exec_func_no<800) dispatrain(g,MIKA_mesta); /* ローマ字練習の各項目の実行画面表示 */
	}
	void menexe(Graphics g,String[] menu_mes,int[][] menu_cord,int[] menu_function,int[] sel_flag,String menut)
	{
		int i,j;
		int x;
		int y;
		String	mesi5="番号キーを押して下さい";
		MIKA_max_x_flag=0; /* 縦 25行モードに設定 */
		MIKA_max_y_flag=0; /* 横 80カラムモードに設定 */
		cslclr(g); /* 画面クリア */
		cslcolor(g,MIKA_magenta); /* 表示色をマゼンタに設定 */
		cslmencenter(g,1,menut); /* メニュータイトルを上端の中央に表示 */
		MIKA_max_x_flag=1; /* 縦 20行モードに設定 */
		MIKA_max_y_flag=1; /* 横 64カラムモードに設定 */
		cslcolor(g,MIKA_cyan);
		cslput(g,18*16,29*8,mesi5); /* 番号キーを押して下さいのメッセージを表示 */
		j=menu_mes.length;
		for(i=0;i<j;i++)
		{
			x=menu_cord[i][0]; /* メニュー表示位置 x座標取得 */
			y=menu_cord[i][1]; /* メニュー表示位置 y座標取得 */
			if(sel_flag[i]==1)	cslcolor(g,MIKA_green); /*前回選択メニュー項目は緑色で表示 */
			else 	cslcolor(g,MIKA_blue); /* その他のメニューは青色で表示 */
			cslput(g,x,y,menu_mes[i]); /* メニュー項目表示 */
			if(sel_flag[i]==1) cslputu(g,x,y,menu_mes[i],1,MIKA_green); /* 前回選択メニュー項目に下線を表示 */
			cslputzscale(g,x,y-4*MIKA_width_y,(char)(i+'1'),1.0); /* メニュー番号を表示 */
		}
		MIKA_menu_function_table=menu_function; /* 機能番号テーブル設定 */
		MIKA_sel_flag=sel_flag; /* 前回選択メニュー項目選択フラグアドレス設定 */
		MIKA_max_x_flag=0; /* 縦 25行モードに戻す */
		MIKA_max_y_flag=0; /* 横 80カラムモードに戻す */
}
	int mencom(int[] menu_function_table,int[] sel_flag,char nChar) /* 選択されたメニューの項目に対応する機能番号を取得 */
	{
		int func_no=0;
		int i,ii,iii;
		int sel_flag1=0;
		if(menu_function_table==null) return(0);
		ii=menu_function_table.length;
		if(nChar==0x1b){ /* 入力文字がエスケープの場合 */
			for(i=0;i<ii;i++) /* メニューに戻りますのメニュー項目をサーチ */
			{
				if(menu_function_table[i]>9000&&menu_function_table[i]<9999) /* メニューに戻りますのメニュー項目があった場合 */  
				{
					func_no=menu_function_table[i];
				}
			}
			return(func_no);
		}
		else if(nChar<=0x30||nChar>0x39) return(0); /* 入力文字が1～9の数字以外は処理をしないでリターン */
		else
		{
			iii=nChar-0x31; /* 文字を数字に変換 */
			if(iii<ii) /* 入力された数字に対応するメニューがある場合 */
			{
				func_no=menu_function_table[iii]; /* 対応する機能番号を取り出す */
				for(i=0;i<ii;i++)
				{
						if(sel_flag[i]!=0) sel_flag1=i+1; /* 前回選択メニュー項目番号をサーチ */
				}
				if(0<func_no&&func_no<9000) /* 今回選択メニューがメニューに戻るで無い場合 */
				{
					if(sel_flag1!=0) sel_flag[sel_flag1-1]=0; /*前回選択メニュー番号をクリア */
					sel_flag[iii]=1; /* 今回の選択メニュー番号を前回選択メニュー番号に設定 */
				}
				return(func_no);
			}
			else
			return(0);
		}	
	}	
int exec_func(Graphics g,char nChar) /* 一文字入力に対応した処理を行う */
	{
		int func_no;
		if(MIKA_exec_func_no==0) /* 最初の初期画面を表示中にキーが押された場合 */
		{
			MIKA_exec_func_no=1; /* 初期画面の表示番号を設定 */
			dispmen(g); /* メニュー表示 */
			return(1);
		}
		func_no=mencom(MIKA_menu_function_table,MIKA_sel_flag,nChar); /* 選択されたメニューの項目に対応する機能番号を取得 */
		if(func_no!=0) /* メニュー表示中に数字キーが押されて対応する機能番号がゼロでない場合 */
		{
			MIKA_menu_function_table=null;
			MIKA_exec_func_no=func_no;
			if(MIKA_exec_func_no==9999) procexit(); /* 機能番号が 9999の時は終了 */
			if (MIKA_exec_func_no>9000) MIKA_exec_func_no=MIKA_exec_func_no-9000; /* 機能番号がメニューに戻るの時は、メニュー番号を取得 */
			if(MIKA_exec_func_no>400&&MIKA_exec_func_no<800) /* 機能番号が練習メニューの実行の場合は各練習の項目ごとに前処理を行う */
			{
				preptrain(MIKA_exec_func_no); /* 練習の各項目ごとの前処理 */
			}
			dispmen(g); /* メニュー、練習画面表示 */
			return(1);
		}
		else /* 練習の実行中にキーが押された場合 */
		{
			if(nChar==0x1b&&MIKA_exec_func_no==29) /* 成績表示中にエスケープキーが押された場合 */
			{
				MIKA_exec_func_no=1; /* 初期メニューのメニュー番号設定 */
				dispmen(g); /* メニュー表示 */
				return(1);
			}
			if(MIKA_exec_func_no>400&&MIKA_exec_func_no<500) /* ポジション練習 */
			{
				procptrain(g,nChar); /* ポジション練習 文字入力処理 */
				return(1);
			}
			if(MIKA_exec_func_no>500&&MIKA_exec_func_no<600) /* ランダム練習 */
			{
				try { /* 2023/6/5 追加 */
					MIKA_semaphore.acquire(); /* セマフォー要求 */ /* 2023/6/5 追加 */
					proctrain(g,nChar); /* ランダム練習 文字入力処理 */
					MIKA_semaphore.release(); /* セマフォー解放 */ /* 2023/6/5 追加 */
				}						
				catch(InterruptedException e)
				{	 /* 2023/6/5 追加 */
					e.printStackTrace(); /* 2023/6/5 追加 */
				}
				return(1);
			}
			if(MIKA_exec_func_no>600&&MIKA_exec_func_no<700) /* 英単語練習 */
			{
				try { /* 2023/6/5 追加 */
					MIKA_semaphore.acquire(); /* セマフォー要求 */ /* 2023/6/5 追加 */
					proctrain(g,nChar); /* 英単語演習 文字入力処理 */
					MIKA_semaphore.release(); /* セマフォー解放 */ /* 2023/6/5 追加 */
				}						
				catch(InterruptedException e)
				{	 /* 2023/6/5 追加 */
					e.printStackTrace(); /* 2023/6/5 追加 */
				}
				return(1);
			}
			if(MIKA_exec_func_no>700&&MIKA_exec_func_no<800) /* ローマ字練習 */
			{
				try { /* 2023/6/5 追加 */
					MIKA_semaphore.acquire(); /* セマフォー要求 */ /* 2023/6/5 追加 */
					procatrain(g,nChar); /* ローマ字練習 文字入力処理 */
					MIKA_semaphore.release(); /* セマフォー解放 */ /* 2023/6/5 追加 */
				}						
				catch(InterruptedException e)
				{	 /* 2023/6/5 追加 */
					e.printStackTrace(); /* 2023/6/5 追加 */
				}
				return(1);
			}
		}
		return(0);
	}
	String  spacepadding(int i) /* 指定文字数のスペース文字列をリターン */
	{
			String a="                                                  "; /* 50文字のスペース文字列 */
			String b=""; /* 空文字列 */
			String aa;
			if(i<=0) return b; /* 指定文字数がゼロ以下の場合は空文字列を返す */
			else if(i>=50) return a; /* 指定文字数が50以上の場合は50文字のスペースを返す */
			else
			{
				aa=a.substring(0,i); /* 指定文字数のスペース文字列を切り出し */
				return aa;
			}
	}
	long timeinterval(long time_start,long time_end) /* ミリ秒で指定された時間間隔の経過時間を秒に変換 */
	{
			long time_interval;
			time_interval=(time_end-time_start)/1000; /* 開始時間ミリ秒と終了時間ミリ秒の差を秒に変換 */
			if(time_interval<=0) time_interval=1; /* 経過時間がゼロ秒以下の場合は1秒を設定 */
			return time_interval;
	}
	String	stringseiseki3(String[] menu_mes,double[] r_speed,String[] r_date,long[] r_time,int i) /* ランダム練習 英単語練習 ローマ字練習の成績を成績ファイルに書き込むための文字列を作成 */
	{
			String a,aa,aaa,sp;
			int	length;
			a=tconv(r_time[i]); /* 合計練習時間を文字列に変換 */
			aa=menu_mes[i];
			length=stringlength(aa); /* 練習項目の文字列長を取得（全角文字を二文字に数える) */
			sp=spacepadding(34-length); /* 練習項目の文字列長を揃えるためにスペース文字を追加 */
			aaa=String.format("%s%s%7.1f %s %s\n",aa,sp,r_speed[i],r_date[i],a); /* 練習成績文字列作成 */
			return aaa;
	}
	int wseiseki() /* 練習終了時に成績ファイルを書き込み */
	{
  	  	String a,aa,aaa,sp;
		long time_i;
		int err;
		BufferedWriter bw;
		OutputStreamWriter filewriter;
		FileOutputStream file;
		int i,length;
//		System.out.printf("proc Exit\n");
		bw=null;
		err=0;
		try {
			file = new FileOutputStream(MIKA_file_name_seiseki2); /* 成績ファイルをオープン */
	  	 	filewriter = new OutputStreamWriter(file,"SHIFT_JIS"); /* 成績ファイルの読み込みフォーマットをシフトJISに指定でオープン */
			bw = new BufferedWriter(filewriter);
// 	  		System.out.printf("file_write\n");
			time_i=timeinterval(MIKA_st_t,MIKA_lt_t); /* 練習開始から終了までの経過秒を取得 */
			a=tconv(MIKA_rt_t+time_i); /* 前回の練習時間に今回の練習時間を加算して文字列に変換 */
			aa=String.format("練習時間　%s\n",a);
			bw.write(aa); /* 成績ファイル書き込み */
			aa=String.format("%s",MIKA_menu_mes_s[0]); /* ポジション練習の練習項目名を文字列に変換 */
			length=stringlength(aa); /* ポジション練習の練習項目名の文字列長を取得 （全角文字を二文字に数える）*/
			sp=spacepadding(51-length); /* 文字列長を揃えるためにスペース文字を追加 */
			a=tconv(MIKA_p_time); /* ポジション練習累積練習時間を文字列に変換 */
			aaa=String.format("%s%s%s%n",aa,sp,a); /*ポジション練習成績を文字列に変換 */
			bw.write(aaa); /* 成績ファイル書き込み */
			for(i=0;i<MIKA_r_speed.length;i++) /* ランダム練習成績書き込み */
			{
				aaa=stringseiseki3(MIKA_menu_mes,MIKA_r_speed,MIKA_r_date,MIKA_r_time,i); /* ランダム練習成績を文字列に変換 */
   				bw.write(aaa); /* 成績ファイル書き込み */
	  		}
			for(i=0;i<MIKA_w_speed.length;i++) /* 英単語練習成績書き込み */
			{
				aaa=stringseiseki3(MIKA_menu_mes_w,MIKA_w_speed,MIKA_w_date,MIKA_w_time,i); /* 英単語練習成績を文字列に変換 */
   				bw.write(aaa); /* 成績ファイル書き込み */
	  		}
			for(i=0;i<MIKA_a_speed.length;i++) /* ローマ字練習成績書き込み */
			{
				aaa=stringseiseki3(MIKA_menu_mes_r,MIKA_a_speed,MIKA_a_date,MIKA_a_time,i); /* ローマ字練習成績を文字列に変換 */
   				bw.write(aaa); /* 成績ファイル書き込み */
	  		}
		} catch (IOException e) { /* 書き込みエラーの場合 */
//        	e.printStackTrace();
			err=1; /* エラーコードを1に設定 */
		} finally { /* 成績ファイルの書き込みが終了した場合 */
			if(bw!=null) /* ファイルがオープンされた場合はクローズ処理を行う */
			{
				try {
					bw.close();
				} catch (IOException e) { /* ファイルクローズ処理がエラーの場合 */
//		           	e.printStackTrace();
					return(1); /* エラーコードを1に設定 */
				}
			}
			return(err); /* エラーコードを返してリターン */
		}
	}
	int wkiroku() /* 練習終了時に練習開始時刻と練習時間を成績記録ファイルに書き込む */
	
	{
  	  	String a,aa,aaa,sp;
		int err;
		String type_rensyu_date;
		BufferedWriter bw;
		OutputStreamWriter filewriter;
		FileOutputStream file;
		long time_i;
		int i,length;
//		System.out.printf("proc Exit\n");
		err=0;
		SimpleDateFormat rensyu_date=new SimpleDateFormat("yy/MM/dd HH:mm:ss"); /* 練習開始日時 取り出しフォーマット作成 */
		type_rensyu_date=rensyu_date.format(MIKA_s_date); /* 練習開始日時を文字列を指定フォーマットに従って作成 */
		time_i=timeinterval(MIKA_st_t,MIKA_lt_t); /* 練習時間取得 */
		a=t0conv(time_i,1); /* 練習時間を"%3d時間%2d分%2d秒"のフォーマットで文字列に変換 */
		aa=String.format("%s 練習時間%s\n",type_rensyu_date,a); /* 書き込みメッセージ作成 */
		bw=null;
		try {
			file = new FileOutputStream(MIKA_file_name_kiroku,true); /* 追記モードで練習時間記録ファイルをオープン */
	  	 	filewriter = new OutputStreamWriter(file,"SHIFT_JIS"); /* 練習時間記録ファイルの書き込みモードをシフトJISに指定 */
			bw = new BufferedWriter(filewriter);
// 	  		System.out.printf("file_write\n");
			bw.write(aa); /* 練習時間記録ファイルに練習記録メッセージを書き込み */
		} catch (IOException e) {
//          	e.printStackTrace();
				err=1;
		} finally {
			if(bw!=null)
			{
				try {
					bw.close(); /* 練習時間記録ファイルをクローズ */
				} catch (IOException e) {
//		           	e.printStackTrace();
					return(1);
				}
			}
			return(err);
		}
	}
	int whayasa() /* 最高入力速度を最高速度記録ファイル書き込む */
	{
  	  	String a,aa,aaa,sp;
		int err;
		String type_rensyu_date;
		BufferedWriter bw;
		OutputStreamWriter filewriter;
		FileOutputStream file;
		long time_i;
		int length;
		err=0;
//		System.out.printf("proc Exit\n");
		SimpleDateFormat rensyu_date=new SimpleDateFormat("yy/MM/dd HH:mm:ss");/* 最高記録達成日時 取り出しフォーマット作成 */
		type_rensyu_date=rensyu_date.format(MIKA_type_kiroku_date); /* 最高記録達成時刻文字列を指定フォーマットに従って作成 */
		a=tconv(MIKA_type_time_record[MIKA_type_kind_no]); /* 累積練習時間を文字列に変換 */
		length=stringlength(MIKA_type_kind_mes); /* 練習項目の文字列長を取得 */
		sp=spacepadding(34-length);/* 練習項目の文字列長を揃えるためにスペース文字を追加 */
		aa=String.format("%s %s%s %5.1f %s\n",type_rensyu_date,MIKA_type_kind_mes,sp,MIKA_type_speed,a); /* 書き込みメッセージ作成 */
		bw=null;
		try {
			file = new FileOutputStream(MIKA_file_name_hayasa,true); /* 追記モードで最高速度記録ファイルをオープン */
	  	 	filewriter = new OutputStreamWriter(file,"SHIFT_JIS"); /* 最高速度記録ファイルの書き込みモードをシフトJISに設定 */
			bw = new BufferedWriter(filewriter);
// 	  		System.out.printf("file_write\n");
			bw.write(aa); /* 最高速度記録ファイルに最高速度記録メッセージを書き込み  */
		} catch (IOException e) {
//         	e.printStackTrace();
			err=1;
		} finally {
			if(bw!=null)
			{
				try {
					bw.close(); /* 最高速度記録ファイルクローズ */
				} catch (IOException e) {
//		           	e.printStackTrace();
					return(1);
				}
			}
			return(err);
		}
	}
	void savekiroku() /* プログラムがウィンドーの閉じるボタンにより終了した場合、練習記録を保存する */
	{
		if(400<MIKA_exec_func_no&&MIKA_exec_func_no<500) /* ポジション練習の場合 */
		{
//			System.out.printf("posision practice\n");
			if(MIKA_practice_end_flag==0&&MIKA_time_start_flag!=0) /* 練習中で練習時間の計測を開始した場合 */
			{
//				System.out.printf("position practic time save\n");
				MIKA_type_end_time=System.currentTimeMillis(); /* 練習終了時間をミリ秒で取得 */
				MIKA_p_time=MIKA_p_time+timeinterval(MIKA_type_start_time,MIKA_type_end_time); /* 累積練習時間の記録
を加算 */
			}
			if(MIKA_practice_end_flag==0) MIKA_practice_end_flag=1;/* 2023/6/5 追加 */
		}
		else if(500<MIKA_exec_func_no&&MIKA_exec_func_no<800) /* ランダム練習 英単語練習 ローマ字練習の場合 */
		{
//			System.out.printf("random word romaji practice\n");
			if(MIKA_practice_end_flag==0&&MIKA_time_start_flag!=0) /* 練習中で練習時間の計測を開始した場合 */
			{
//				System.out.printf("random word romaji  practic time save\n");
				MIKA_type_end_time=System.currentTimeMillis(); /* 練習終了時間をミリ秒で取得 */
				MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+timeinterval(MIKA_type_start_time,MIKA_type_end_time); /* 練習終了時間をミリ秒で取得 */
			}
			if(MIKA_type_syuryou_flag==1||MIKA_type_syuryou_flag==2) /* 最高記録を更新して練習を終了した場合 */
			{
				MIKA_type_speed_record[MIKA_type_kind_no]=MIKA_type_speed; /* 最高入力速度を保存 */
				MIKA_type_date_record[MIKA_type_kind_no]=MIKA_type_date; /* 達成日を保存 */
			}
			if(MIKA_practice_end_flag==0) MIKA_practice_end_flag=1;/* 2023/6/5 追加 */
		}
	}

	String 	mesfileerr() /* ファイル書き込みエラーメッセージ作成 */
	{
		String a,a1,a2,a3;
		if(MIKA_file_error_seiseki==1||MIKA_file_error_kiroku==1||MIKA_file_error_hayasa==1)
		{	
			if(MIKA_file_error_seiseki==1) a1=String.format("%s\n",MIKA_file_name_seiseki); /* 成績ファイル名設定 */
			else a1="";
			if(MIKA_file_error_kiroku==1) a2=String.format("%s\n",MIKA_file_name_kiroku); /* 練習時間記録ファイル名設定 */
			else a2="";
			if(MIKA_file_error_hayasa==1) a3=String.format("%s\n",MIKA_file_name_hayasa); /* 最高速度記録ファイル名を設定 */
			else a3="";
			a=String.format("成績ファイル\n%s%s%sの書き込みができませんでした。",a1,a2,a3); /* エラーメッセージ作成 */
		}
		else a=""; /* エラーが無い場合は空メッセージ */
		return a;
	}
	void procexit() /* プログラム終了時の処理 */
	{
		String a;
		Container c;
		MIKA_lt_t=System.currentTimeMillis(); /* 練習時間記録ファイル用練習終了時間をミリ秒で取得 */
		MIKA_file_error_seiseki=wseiseki(); /* 成績ファイル書き込み */
		MIKA_file_error_kiroku=wkiroku(); /* 練習時間記録ファイル書き込み */
//		MIKA_file_error_seiseki=1;
//		MIKA_file_error_kiroku=1;
//		MIKA_file_error_hayasa=1;
		if(MIKA_file_error_seiseki==1||MIKA_file_error_kiroku==1||MIKA_file_error_hayasa==1) /* 成績ファイル書き込みエラーの場合 */
		{
			a=mesfileerr(); /* 成績ファイル書き込みエラーメッセジ作成 */
			c = getContentPane();
			JOptionPane.showMessageDialog(c.getParent(),a,"成績ファイル書き込みエラー",JOptionPane.WARNING_MESSAGE);
			/* 成績ファイル書き込みエラーダイアログ表示 */
		}
		System.exit(0); /* プログラム終了 */
	}		
	void preptrain(int func_no) /* 練習の前処理 */
	{
			if(MIKA_exec_func_no>400&&MIKA_exec_func_no<500) /* ポジション練習の前処理 */
			{
				MIKA_type_kind_no=func_no-401; /* 練習項目番号を取得 */
				MIKA_practice_end_flag=0; /* 練習実行中フラグクリア */	
				MIKA_menu_kind_flag=MIKA_key_guide_on; /* キーガイドを表示するモードに指定 */
				MIKA_key_guide_flag=0; /* 練習終了時に「この次はスペースキーを押してキーガイドを表示してあるいは消去して練習しましょうね」の表示を行うフラグ を消去 */
				MIKA_type_end_flag=0; /* 練習終了フラグをクリア */
				MIKA_time_start_flag=0; /* 時間計測開始フラグをクリア */
				MIKA_type_kind_mes=MIKA_menu_mes[MIKA_type_kind_no]; /* 練習項目名を設定 */
				MIKA_p_count=MIKA_p_count_position; /* 練習回数配列アドレスにポジション練習 練習回数 を設定 */
				MIKA_char_table=MIKA_h_pos[MIKA_type_kind_no]; /* 練習文字列テーブルアドレスに ポジション練習 ランダム練習 練習文字列テーブルの指定項目を設定 */
				MIKA_char_position=randomchar(MIKA_char_table,-1); /* 最初の練習文字の練習文字テーブル内番号をランダムに取得 */
				MIKA_key_char=MIKA_char_table.charAt(MIKA_char_position); /* 練習文字テーブル内番号に対応する練習文字を取得 */
				MIKA_guide_char=MIKA_key_char; /* ガイドキー文字に練習文字を設定 */
				MIKA_err_char=0; /* エラー文字にゼロを指定 */
				MIKA_type_err_count=0; /* エラー文字カウンターをゼロクリア */
				MIKA_type_count=0; /* 練習文字カウンターをゼロクリア */
			}
			if(MIKA_exec_func_no>500&&MIKA_exec_func_no<600) /* ランダム練習の前処理 */
			{
				MIKA_type_kind_no=func_no-501; /* 練習項目番号を取得 */
				MIKA_type_speed_record=MIKA_r_speed; /* 最高速度記録配列アドレスに ランダム練習 最高速度記録 を設定 */
				MIKA_type_date_record=MIKA_r_date; /* 最高速度達成日配列アドレスに  ランダム練習 最高速度達成日付 を設定 */
				MIKA_type_time_record=MIKA_r_time;  /* 累積練習時間配列アドレスに ランダム練習 累積練習時間 を設定 */
				MIKA_p_count=MIKA_p_count_random; /* 練習回数配列アドレスにランダム練習 練習回数 を設定 */
				MIKA_practice_end_flag=0; /* 練習実行中フラグクリア */	
				MIKA_type_kind_mes=MIKA_menu_mes[MIKA_type_kind_no]; /* 練習項目名を設定 */
				MIKA_char_table=MIKA_h_pos[MIKA_type_kind_no]; /* 練習文字列テーブルアドレスに ポジション練習 ランダム練習 練習文字列テーブルの指定項目を設定 */
				inctable(MIKA_char_table,MIKA_type_speed_record[MIKA_type_kind_no]); /* ランダム練習 練習テキスト作成 */
				prepflags(0); /* 練習フラグ初期化 */
			}
			if(MIKA_exec_func_no>600&&MIKA_exec_func_no<700) /* 英単語練習の前処理 */
			{
				MIKA_type_kind_no=func_no-601; /* 練習項目番号を取得 */
				MIKA_type_speed_record=MIKA_w_speed; /* 最高速度記録配列アドレスに 英単語練習 最高速度記録 を設定 */
				MIKA_type_date_record=MIKA_w_date; /* 最高速度達成日配列アドレスに 英単語練習 最高速度達成日付 を設定 */
				MIKA_type_time_record=MIKA_w_time; /* 累積練習時間配列アドレスに 英単語練習 累積練習時間 を設定 */
				MIKA_p_count=MIKA_p_count_word; /* 練習回数配列アドレスに英単語練習 練習回数 を設定 */
				MIKA_practice_end_flag=0; /* 練習実行中フラグクリア */	
				MIKA_type_kind_mes=MIKA_menu_mes_w[MIKA_type_kind_no]; /* 練習項目名を設定 */
				MIKA_word_table=MIKA_w_seq[MIKA_type_kind_no]; /* 練習単語テーブルアドレスに英単語練習単語テーブルの指定項目を設定 */
				intwordtable(MIKA_word_table,MIKA_type_speed_record[MIKA_type_kind_no]); /* 英単語練習 練習テキスト作成 */
				prepflags(0); /* 練習フラグ初期化 */
			}
		if(MIKA_exec_func_no>700&&MIKA_exec_func_no<800) /* ローマ字練習の前処理 */
			{
				MIKA_type_kind_no=func_no-701; /* 練習項目番号を取得 */
				MIKA_type_speed_record=MIKA_a_speed; /* 最高速度記録配列アドレスに ローマ字練習 最高速度記録 を設定 */
				MIKA_type_date_record=MIKA_a_date; /* 最高速度達成日配列アドレスに ローマ字練習 最高速度達成日付 を設定 */
				MIKA_type_time_record=MIKA_a_time; /* 累積練習時間配列アドレスに ローマ字練習 累積練習時間 を設定 */
				MIKA_p_count=MIKA_p_count_romaji; /* 練習回数配列アドレスにローマ字練習 練習回数 を設定 */
				MIKA_practice_end_flag=0; /* 練習実行中フラグクリア */	
				MIKA_type_kind_mes=MIKA_menu_mes_r[MIKA_type_kind_no]; /* 練習項目名を設定 */
				if(MIKA_type_kind_no==0) /* ローマ字ランダム練習の場合 */
				{
					inatable(MIKA_kana,MIKA_type_speed_record[MIKA_type_kind_no]); /* ローマ字ランダム練習 練習テキスト作成 */
				}
				else /* ローマ字単語練習の場合 */
				{
					intawordtable(MIKA_romaji_tango_table,MIKA_type_speed_record[MIKA_type_kind_no]); /* ローマ字単語練習練習テキスト作成 */
				}
				prepflags(0); /* 練習フラグ初期化 */
				getromaji(MIKA_w_count); /* 最初の練習文字のローマ字読み取得 */
			}
	}
	void keyguideoff(Graphics g) /* ポジション練習のキーガイドをオフにする */
	{
			dispkeygideonoff(g,1); /* スペースキーを押すとキーガイドを表示しますのメッセージを表示 */
			diposit(g,3); /* キーボードイラストの刻印を消去する */
			MIKA_guide_char=0; /* ガイドキー文字にゼロを設定 */
			dikposit(g,MIKA_err_char,3); /* エラー文字を表示する */
	}
	void keyguideon(Graphics g) /* ポジション練習のキーガイドをオンにする */
	{
			dispkeygideonoff(g,1); /* スペースキーを押すとガイドキーを消去しますのメッセージを表示 */
			diposit(g,2); /* キーボードイラストの刻印を表示する */
			MIKA_guide_char=MIKA_key_char; /* ガイドキー文字に練習文字を設定*/
			dikposit(g,MIKA_guide_char,0); /* ガイドキー文字を表示する */
			dikposit(g,MIKA_err_char,3); /* エラー文字を表示する */
	}
	void dispretrymessage(Graphics g,int flag) /* リトライメッセージ表示 flag=0 表示を行う flag=1 表示を消去 */
	{
		if(flag==0) cslcolor(g,MIKA_cyan); /* 表示色をシアンに設定 */
		else cslcolor(g,MIKA_bk_color); /* 表示色を背景色に設定 */
		cslput(g,22*16,10*8,MIKA_mesi1); /* 「もう一度練習するときはリターンキーまたは、Enterキーを押してください」のメッセージを表示 */
		cslput(g,23*16,10*8,MIKA_mesi2); /* 「この次は、スペースキーを押してキーガイドを表示して練習してみましょうね」のメッセージを表示 */
	}
	int funcbackmenu(int func_no) /* メニューの階層を一段上に戻る */
	{
		int ffun_no=0;
		if(func_no>400&&func_no<500) /* ポジション練習の各項目の場合 */
		{
			ffun_no=21; /* ポジション練習のメニューに戻る */
		}
		else if(func_no>500&&func_no<600) /* ランダム練習の各項目の場合 */
		{
			ffun_no=22; /* ランダム練習のメニューに戻る */
		}
		else if(func_no>600&&func_no<700) /* 英単語練習の各項目の場合 */
		{
			ffun_no=23; /* 英単語練習のメニューに戻る */
		}
		else if(func_no>700&&func_no<800) /* ローマ字の各項目の場合 */
		{
			ffun_no=24; /* ローマ字練習のメニューに戻る */
		}
		else
		{
			ffun_no=1; /* 初期メニューに戻る */
		}
		return ffun_no;
	}
	void procpabort(Graphics g) /*エスケープで終了しますの表示消去  指表示消去 リトライメッセージ表示 */
	{
				dispabortmes(g,1); /* エスケープで終了しますの表示消去 */
				pfinger(g,1); /* 指のイラストを消去 */
				dispretrymessage(g,0); /* リトライメッセージ表示 */
	}
	void  procpnextchar(Graphics g) /* ポジション練習での次回の練習文字の表示処理 */
	{
			if(MIKA_menu_kind_flag==MIKA_key_guide_off) /* キーガイド表示がオフの場合 */
			{
				dikposit(g,MIKA_err_char,2); /* エラー文字表示をキーの刻印なしで消去 */
				dikposit(g,MIKA_guide_char,2); /* ガイドキー文字表示をキーの刻印なしで消去 */
				if(MIKA_guide_char!=0) /* ガイドキー文字表示中の場合 */
				difposit(g,MIKA_guide_char,1); /* 指の位置表示を消去 */
			}
			else
			{
				dikposit(g,MIKA_err_char,1); /* エラー文字表示をキーの刻印ありで消去 */
				dikposit(g,MIKA_guide_char,1); /* ガイドキー文字表示をキーの刻印ありで消去 */
				difposit(g,MIKA_guide_char,1); /* 指の位置表示を消去 */
			}
			MIKA_err_char=0;			
			dispguidechar(g,MIKA_key_char,1); /* 練習文字表示を消去 */
			MIKA_char_position=randomchar(MIKA_char_table,MIKA_char_position); /* 次回練習文字番号取得 */
			MIKA_key_char=MIKA_char_table.charAt(MIKA_char_position); /* 次回練習文字取得 */
			if(MIKA_menu_kind_flag==MIKA_key_guide_on) MIKA_guide_char=MIKA_key_char; /* キーガイド表示中の場合はガイドキー文字に練習文字を代入 */
			else MIKA_guide_char=0; /* キーガイド表示なしの場合はガイドキー文字にゼロを代入 */
			dispguidechar(g,MIKA_key_char,0); /* 次回練習文字を表示 */
			dikposit(g,MIKA_guide_char,0); /* ガイドキー文字の位置を表示 */
			difposit(g,MIKA_guide_char,0); /* ガイドキー文字の指位置を表示 */
	}
	char convertupperlower(char a,char b) /* b の文字の種別をa の文字種別に揃える */
	{
			if('A'<=a&&a<='Z'&&'a'<=b&&b<='z') b=(char)(b-'a'+'A'); /* aが大文字でbが小文字の場合はbを大文字に変換 */
			else	if('a'<=a&&a<='z'&&'A'<=b&&b<='Z') b=(char)(b-'A'+'a'); /* aが小文字でbが大文字の場合はbを小文字に変換 */
			return b;
	}
	synchronized void procptrain(Graphics g,char nChar) /* ポジション練習の文字入力処理 */
	{
//			System.out.printf("char %x pressed\n",(int) nChar);
			if(nChar==' ') /* 入力文字がスペースの場合 */
			{
			if(MIKA_practice_end_flag==0){ /* 入力練習実行中の場合 */
				if(MIKA_menu_kind_flag==MIKA_key_guide_on) /* キーガイド表示中の場合 */
				{
					MIKA_menu_kind_flag=MIKA_key_guide_off; /* キーガイド表示フラグをキーガイド表示無しに設定 */
//					MIKA_timer=new Timer();
					MIKA_Procptimer = new Procptimer(); /* ガイドキー文字位置表示用のタイマーを取得 */
					if(MIKA_type_count==0) MIKA_timer.schedule(MIKA_Procptimer,3000); /* 最初の文字はタイマーを三秒に設定 */
					else MIKA_timer.schedule(MIKA_Procptimer,2000); /* 二度め以降はタイマーを二秒に設定 */
					difposit(g,MIKA_guide_char,1); /* 練習文字に対応した指の爪の表示を消去 */
					keyguideoff(g); /* ポジション練習のキーガイドをオフにする */
				}
				else /* キーガイド表示無しの場合 */
				{
					if(MIKA_guide_char==0){ /* ガイドキー文字位置が未表示の場合 */
						MIKA_Procptimer.cancel(); /* ガイドキー文字位置表示用タイマーをキャンセル */
					}
					MIKA_menu_kind_flag=MIKA_key_guide_on; /* キーガイド表示フラグをキーガイド表示ありに設定 */
					keyguideon(g); /* ポジション練習のキーガイドをオンにする */
					difposit(g,MIKA_guide_char,0); /* 練習文字に対応した指の爪の位置を表示 */
				}
			}
			else if(MIKA_practice_end_flag==1){ /* 練習終了時の場合 */
				if(MIKA_menu_kind_flag==MIKA_key_guide_on){ /* キーガイド表示中の場合 */
						MIKA_menu_kind_flag=MIKA_key_guide_off; /* キーガイド表示フラグをキーガイド表示無しに設定 */
						keyguideoff(g); /* ポジション練習のキーガイドをオフにする */
				}
				else /* キーガイド表示無しの場合 */
				{
						MIKA_menu_kind_flag=MIKA_key_guide_on; /* キーガイド表示フラグをキーガイド表示ありに設定 */
						keyguideon(g); /* ポジション練習のキーガイドをオンにする */
				}
			}
		}
		else if(nChar==0x1b){ /* エスケープキー入力の場合 */
			if(MIKA_practice_end_flag==0) /* 入力練習実行中の場合 */
			{
				MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
				if(MIKA_menu_kind_flag==MIKA_key_guide_off&&MIKA_guide_char==0) /* キーガイド表示無しでガイドキー文字文位置未表示の場合 */
				{
					MIKA_Procptimer.cancel(); /* ガイドキー文字位置表示用タイマーをキャンセル */
				}
				if(MIKA_time_start_flag!=0) /* 最初の正解を入力済の場合 */
				{
					MIKA_type_end_time=System.currentTimeMillis(); /* 練習終了時間をミリ秒で取得 */
//					MIKA_type_speed_time=(MIKA_type_end_time-MIKA_type_start_time)/1000.0;/* 練習経過時間 秒を計算 */ /* 2023/2/24修正 旧コード */
					MIKA_type_speed_time=roundtime((MIKA_type_end_time-MIKA_type_start_time)/1000.0);/* 練習経過時間 秒を計算 */ /* 2023/2/24修正 新コード */
					MIKA_p_time=MIKA_p_time+(long)MIKA_type_speed_time; /* 累積練習時間の記録を加算 */
				}
				procpabort(g); /* 指表示消去 エスケープで終了しますの表示消去 リトライメッセージ表示 */
			}
			else if(MIKA_practice_end_flag==1) /* 練習終了の場合 */
			{
				MIKA_exec_func_no=funcbackmenu(MIKA_exec_func_no); /* メニューを一階層戻る */
				dispmen(g); /* メニュー表示 */
			}
		}
		else if((nChar==0x0d||nChar==0x0a)&&MIKA_practice_end_flag==1)	 /* 練習の終了時に改行が入力された場合 */
		{
			MIKA_practice_end_flag=0; /* 練習実行中フラグをクリア */
			MIKA_type_end_flag=0; /* 練習終了フラグをクリア */
			dispkeyguidonoffmes(g,1); /* この次はキーガイドを表示して練習しましょう、キーガイドを消去して練習しましょうの表示を消去 */
	 		dispretrymessage(g,1); /* リトライメッセージ消去 */
			dispsecond(g,1); /* 前回練習時間表示消去 */
			dispabortmes(g,0); /* エスケープキーを押すと中断しますのメッセージを表示 */
			pfinger(g,0); /* 指のイラストを表示 */
			dispseikai(g,2); /* メッセージと共に前回正解数消去 */
			MIKA_key_guide_flag=0; /* キーガイドメッセージ表示フラグ クリア */
			MIKA_type_count=0; /* 入力文字数カウンタークリア */
			disperror(g,2); /* メッセージと共に前回エラー回数を消去 */
			MIKA_type_err_count=0; /* エラー入力文字数数クリア */
			MIKA_time_start_flag=0; /* 時間計測開始フラグクリア */
			procpnextchar(g); /* 次回の練習文字を表示 */
			if(MIKA_menu_kind_flag==MIKA_key_guide_off) /* キーガイド非表示の場合 */
			{
				MIKA_Procptimer = new Procptimer(); /* ガイドキー文字位置表示用のタイマーを取得 */
				MIKA_timer.schedule(MIKA_Procptimer,3000); /* 最初の文字はタイマーを三秒に設定 */
			}
		}
		else if(MIKA_practice_end_flag==0) /* 練習実行中の場合 */
		{
//			System.out.printf("TYPE char %1c %1c\n",MIKA_key_char,nChar);
			if(uppertolower(MIKA_key_char)==uppertolower(nChar)) /* 練習文字と入力文字を小文字に変換して比較 */
			{
				/* 正解の場合 */
				if(MIKA_menu_kind_flag==MIKA_key_guide_off&&MIKA_guide_char==0) /* キーガイド非表示ガイドキー文字位置未表示の場合 */
				{
					MIKA_Procptimer.cancel(); /* タイマーキャンセル */
				}
				dispseikai(g,1); /* 前回正解数表示消去 */
				if(MIKA_time_start_flag==0) /* 最初の正解文字入力の場合 */
				{
						MIKA_type_start_time=System.currentTimeMillis();  /* 練習開始時間をミリ秒で取得取得 */
						MIKA_time_start_flag=1; /* 練習時間計測フラグセット */
				}
				MIKA_type_count++; /* 正解数を加算 */
				dispseikai(g,0); /* 正解数を表示 */
				if(MIKA_type_count>=MIKA_position_limit) /* 60文字入力した場合は練習を終了 */
				{
					MIKA_type_end_time=System.currentTimeMillis(); /* 練習終了時間をミリ秒で取得取得 */
//					MIKA_type_speed_time=(MIKA_type_end_time-MIKA_type_start_time)/1000.0; /* 練習経過時間を計算 */ /* 2023/2/24修正 旧コード */
					MIKA_type_speed_time=roundtime((MIKA_type_end_time-MIKA_type_start_time)/1000.0); /* 練習経過時間を計算 */ /* 2023/2/24修正 新コード */
					MIKA_p_time=MIKA_p_time+(long)MIKA_type_speed_time; /* 累積練習時間の記録を加算 */
					if(MIKA_menu_kind_flag==MIKA_key_guide_off) /* キーガイド表示がオフの場合 */ /* 2023/2/24追加 */
					{
						dikposit(g,MIKA_err_char,2); /* エラー文字表示をキーの刻印なしで消去 */ /* 2023/2/24追加 */
					}
					else /* 2023/2/24追加 */
					{
						dikposit(g,MIKA_err_char,1); /* エラー文字表示をキーの刻印ありで消去 */ /* 2023/2/24追加 */
					}
					MIKA_err_char=0;  /* エラー文字にゼロを指定 */ /* 2023/2/24追加 */			
					procpabort(g); /* 指表示消去 エスケープで終了しますの表示消去 リトライメッセージ表示 */
					MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
					MIKA_type_end_flag=1; /* 練習終了フラグを60文字入力による終了にセット */
					dispkaisu(g,1); /* 前回練習回数表示クリア */
					MIKA_p_count[MIKA_type_kind_no]++; /* 練習回数加算 */
					dispsecond(g,0); /* 今回練習時間表示 */
					dispkaisu(g,0); /* 今回練習回数表示 */
					if(MIKA_type_err_count<=5&&MIKA_menu_kind_flag==MIKA_key_guide_on) /* エラー回数が5以下でキーガイド表示ありの場合 */
					{
						MIKA_key_guide_flag=1;
						dispkeyguidonoffmes(g,0); /* 「この次は、スペースキーを押してキーガイドの表示を消して練習してみましょうね」メーセージを表示 */
					}
					else if(MIKA_type_err_count>=15&&MIKA_menu_kind_flag==MIKA_key_guide_off) /* エラー回数が15以上でキーガイド表示なしの場合 */
					{
						MIKA_key_guide_flag=2;
						dispkeyguidonoffmes(g,0); /* 「この次は、スペースキーを押してキーガイドを表示して練習してみましょうね」のメッセージを表示 */
					}
				}
				else
				{
					try { /* 2023/6/6 追加 */
						Thread.sleep(20); /* 2023/6/6 追加 */
					} catch(InterruptedException e) { /* 2023/6/6 追加 */
						e.printStackTrace(); /* 2023/6/6 追加 */
					}
					procpnextchar(g); /* 次練習文字を取得して表示 */
					if(MIKA_menu_kind_flag==MIKA_key_guide_off) /* キーガイド表示なしの場合 */
					{
						MIKA_Procptimer = new Procptimer(); /*  ガイドキー文字位置表示用のタイマーを取得 */
						MIKA_timer.schedule(MIKA_Procptimer,2000); /* 二秒タイマー設定 */

					}

				}
			}
			else /* 入力エラーの場合 */
			{
				disperror(g,1); /* 前回エラー入力文数表示を消去 */
				MIKA_type_err_count++; /* エラー入力文字数カウンターを加算 */
				disperror(g,0); /* 今回エラー入力文字数を表示 */
				if(MIKA_menu_kind_flag==MIKA_key_guide_off) dikposit(g,MIKA_err_char,2); /* キーガイド表示なしの時は前回エラー入力文字を消去 */
				else dikposit(g,MIKA_err_char,1); /* キーガイド表示中は 前回エラー入力文字の赤色エラー表示を元に戻す */
				MIKA_err_char=convertupperlower(MIKA_key_char,nChar); /* エラー文字の文字種 大文字小文字 を練習文字と合せる。 */

//				System.out.printf("error char=%1c\n",MIKA_err_char);
				dikposit(g,MIKA_err_char,3); /* エラー入力文字位置を背景赤で表示 */
			}
		}
	}
//	void dispbkchar(Graphics g,int i,int j) /* ランダム練習 英単語練習 ローマ字練習で練習文字の背景を表示 */ /*2023/2/25旧コード */
	void dispbkchar(Graphics g,int i,int j,double scale) /* ランダム練習 英単語練習 ローマ字練習で練習文字の背景を表示 */ /* 2023/2/25 新コード */
	{
//		int ii,jj; /* 2023/2/25 削除 */
//		ii=xxcord(i); /* 練習文字の縦位置を実座標に変換 */ /* 2023/2/25 削除 */
//		jj=yycord(j); /* 練習文字の横位置を実座標に変換 */ /* 2023/2/25 削除 */
//		cslputzscale(g,ii,jj,'■',1.4); /* 四角文字を倍率1.4倍で表示 */ /* 2023/2/25 削除*/
// 2023/2/25 追加開始 
		int xx,yy;
		int xx1,xx2,yy1,yy2;
		int font_size;
		font_size=cslfontsize(scale); /* 文字フォントサイズ取得 */
		xx1=xcord(i);	/* 左上 x 座標を 仮想座標から実座標に変換 */ 
		xx2=xcord(i+16);	/* 右下 x 座標を 仮想座標から実座標に変換 */
		xx=(xx2+xx1-font_size)/2; /* 四角表示 左上 x 座標取得 */
		yy1=ycord(j);	/* 左上 y 座標を 仮想座標から実座標に変換 */
		yy2=ycord(j+16);	 /* 右下 y 座標を 仮想座標から実座標に変換 */
		yy=(yy2+yy1-font_size)/2; /* 四角表示 左上 y座標取得 */
		g.fillRect(yy,xx,font_size,font_size);	/*四角を描画 */
// 2023/2/25 追加終了
	}
	void dispchar(Graphics g,int i,int j,char a) /* ランダム練習 英単語練習 ローマ字練習で練習文字を表示 */
	{
		int ii,jj;
		ii=xxcord(i); /* 練習文字の縦位置を実座標に変換 */
		jj=yycord(j); /* 練習文字の横位置を実座標に変換 */
		cslputzscale(g,ii,jj,a,MIKA_random_scale); /* 練習文字を等倍で表示 */
	}
	void disperrchar(Graphics g,int flag) /* ランダム練習 英単語練習 ローマ字練習で エラー文字を表示 */
// flag=1 赤色背景で表示
// flag=0 背景白色で表示
	{
		int ii,jj;
		Color color1,color2;
		if(flag==1) /* エラー文字を背景赤色で表示する場合 */
		{
			color1=Color.red; /* 背景色を赤色に指定 */
			color2=Color.black; /* 文字色を黒色に指定 */
		}
		else /* エラー文字を背景白色で再表示する場合 */
		{
			color1=MIKA_bk_color; /* 背景色を白色に指定 */
			color2=Color.black; /* 文字色を黒色に指定 */
		}
		cslcolor(g,color1); /* 背景色の設定 */
		ii=xxcord(MIKA_c_p2); /* 練習文字の縦位置を実座標に変換 */ /* 2023/2/25 追加 */
		jj=yycord(MIKA_c_p1); /* 練習文字の横位置を実座標に変換 */ /* 2023/2/25 追加 */
//		dispbkchar(g,MIKA_c_p2,MIKA_c_p1);	/* 四角い文字を背景に表示 */ /* 2023/2/25 旧コード */
		dispbkchar(g,ii,jj,MIKA_random_scale);	/* 四角い文字を背景に表示 */ /* 2023/2/25 新コード */
		cslcolor(g,color2); /* 文字色の設定 */
//		dispchar(g,MIKA_c_p2,MIKA_c_p1,MIKA_chat_t[MIKA_c_p2][MIKA_c_p1]); /* 練習文字を表示 */ /* 2023/2/25 旧コード */
		cslputzscale(g,ii,jj,MIKA_chat_t[MIKA_c_p2][MIKA_c_p1],MIKA_random_scale); /* 練習文字を表示 */ /* 2023/2/25 新コード */
	}
	void dispspeedrate(Graphics g,int flag) /* ランダム練習 英単語練習 入力速度表示 */
// flag=0 表示 flag=1 消去
	{
		String a;
		int offset;
		if(flag==0)
		{
			cslcolor(g,MIKA_blue); /* flagが=ゼロの時は青色で表示 */
			offset=0;
			a=String.format("入力速度%6.1f文字／分",MIKA_type_speed); /* 入力速度を文字列に変換 */
		}
		else
		{
			cslcolor(g,MIKA_bk_color);; /* flagが=1の場合は表示消去 */
			offset=8;
			a=String.format("%6.1f",MIKA_type_speed); /* 入力速度を文字列に変換 */
		}
		cslput(g,5*16,(24+offset)*8,a); /* 入力速度を表示 */
	}
	void dispspeedrate2(Graphics g,int flag) /* ローマ字練習 打鍵数 入力速度表示 */
// flag=0 表示 flag=1 消去
	{
		String a,b;
		int offset_a,offset_b;
		if(flag==0)
		{
			cslcolor(g,MIKA_blue); /* flagが=ゼロの時は青色で表示 */
			a=String.format("打鍵数%6.1f文字／分",MIKA_type_speed2); /* 打鍵数を文字列に変換 */
			offset_a=0;
			b=String.format("入力速度%6.1f文字／分",MIKA_type_speed); /* 入力速度を文字列に変換 */
			offset_b=0;
		}
		else
		{
			cslcolor(g,MIKA_bk_color); /* flagが=1の場合は表示消去 */
			a=String.format("%6.1f",MIKA_type_speed2); /* 打鍵数を文字列に変換 */
			offset_a=6;
			b=String.format("%6.1f",MIKA_type_speed); /* 入力速度を文字列に変換 */
			offset_b=8;
		}
		cslput(g,5*16,(20+offset_a)*8,a); /* 打鍵数を表示 */
		cslput(g,5*16,(42+offset_b)*8,b); /* 入力速度を表示 */
	}
	double ftypespeed(int count,long start_time,long end_time) /* 一分間あたりのタイプ速度を計算 */
// count 文字数
// start_time 開始時間 ミリ秒
// end_time 終了時間 ミリ秒
	{
		double speed_rate;
		double r_count;
		r_count=count;
		if(end_time==start_time) speed_rate=0.0; /* 開始時間と終了時間が一致する場合はタイプ速度をゼロに指定 */
		else
		{
			speed_rate=1000.0*60.0*r_count/(end_time-start_time); /* 一分間あたりのタイプ速度を計算 */
		}
		return speed_rate;
	}
	String mesdisptime(int u_flag,int flag,double type_speed_time) /* 練習経過時間文字列作成 */
// u_flag=0 練習経過時間を2桁の整数で表示 flag=1 練習経過時間を小数点以下二桁まで表示
	{
		String a;
		if(u_flag==0) /* 打ち切りフラグがゼロの場合 */
		{
			if(flag==0)
			{
				a=String.format("経過時間%2.0f秒",type_speed_time); /* 練習経過時間を整数で表示 */
			}
			else
			{
				if(MIKA_utikiri_flag==1) a=String.format("%2.0f秒",type_speed_time); /* 練習経過時間を小数点以下二桁まで表示 */
				else
				a=String.format("%2.0f",type_speed_time); /* 練習経過時間を整数で表示 */
			}
		}
		else /* 打ち切りフラグが1の場合 */
		{
			if(flag==0)
			{
				a=String.format("経過時間%5.2f秒",type_speed_time); /* 練習経過時間を小数点以下二桁まで表示 */
			}
			else
			{
				if(MIKA_utikiri_flag==1) a=String.format("%5.2f秒",type_speed_time); /* 練習経過時間を小数点以下二桁まで表示 */
				else
				a=String.format("%5.2f",type_speed_time); /* 練習経過時間を小数点以下二桁まで表示 */

			}
		}
		return a;
	}
	void disptime(Graphics g,int flag) /* ランダム練習 英単語練習 ローマ字練習にて練習経過時間を表示 */
// flag=0 表示 flag=1 消去 
	{
		String a;
		int offset;
		if(flag==0) /* 緑色で練習経過時間を表示 */
		{
			cslcolor(g,MIKA_blue); /* 表示色を青に設定 */
			a=mesdisptime(MIKA_utikiri_flag,flag,MIKA_type_speed_time); /* 練習経過時間文字列作成 */
			offset=0;
		}
		else /* 前回の練習経過時間表示を消去 */
		{
			cslcolor(g,MIKA_bk_color); /* 表示色を背景色に設定 */
			a=mesdisptime(MIKA_utikiri_flag2,flag,MIKA_type_speed_time); /* 練習経過時間文字列作成 */
			offset=8;
		}
		cslput(g,5*16,(4+offset)*8,a); /* 文字列の表示あるいは消去 */
	}
	void dispromaji(Graphics g,String a,int flag) /* 練習ひらがな文字のローマ字表記の表示と消去 */
// a ローマ字表記文字列
// flag=0 表示 flag=1 消去
	{
		int i,a_length;
		int ii=2*16+2; /* ローマ字表示仮想 x座標 */
		if(a==null) /* ローマ字表記文字列がnullの場合はスペースを表示 */
		{
			if(flag==0) /* エラー文字の表示とエラー文字表示からの復帰を行う場合 */
			{
				if(MIKA_err_char_flag==1) /* エラー表示の場合は赤色表示 */
				cslcolor(g,MIKA_color_romaji_err);	
				else /* エラー表示の復帰を行う場合は背景色で表示 */
				cslcolor(g,MIKA_bk_color);
//				cslputscale(g,ii,38*8+8,"■",2.0); /* 四角い文字を2.0倍の倍率で表示 */ /* 2023/2/25 旧コード */
				dispbkchar(g,ii,38*8+8,MIKA_romaji_bk_scale); /* 文字の背景を赤色で表示 */ /* 2023/2/25 新コード */
	 	 		return;
			}
			else /* 表示の消去を行う場合 */
			{
				cslputu(g,ii+4,38*8,"aaaa",1,MIKA_bk_color); /* 下線を消去 */
				return;
			}
		}
		a_length=a.length(); /* ローマ字表記の文字列長を取得 */
		if(flag==0) /* 表示を行う場合 */
		{
			for(i=0;i<a_length;i++)
			{
				if(MIKA_err_char_flag==1&&i==MIKA_r_count) cslcolor(g,MIKA_color_romaji_err); /* エラー文字は背景を赤色表示 */
				else cslcolor(g,MIKA_bk_color); /* エラー文字でない時は背景色で表示 */
//				cslputscale(g,ii,38*8+8+i*32,"■",2.0); /* 四角い文字を2.0倍の倍率で表示 */ /* 2023/2/25 旧コード */
				dispbkchar(g,ii,38*8+8+i*32,MIKA_romaji_bk_scale); /* 文字の背景を赤色で表示 */ /* 2023/2/25 新コード */
				cslcolor(g,MIKA_color_romaji); /* ローマ字表示の色指定 */
				cslputzscale(g,ii,38*8+8+i*32,a.charAt(i),MIKA_romaji_scale); /* ローマ字表記のi番目の文字を2.0倍の倍率で表示 */
				if(i<MIKA_r_count) cslputu(g,ii+4,38*8+i*32,"aaaa",1,MIKA_color_romaji_under_line); /* 入力済みの文字にはアンダーラインを表示 */
			}
		}
		else /* 表示の消去を行う場合 */
		{
			cslcolor(g,MIKA_bk_color); /* 表示色を背景色に設定 */
			for(i=0;i<a_length;i++)
			{
				cslputzscale(g,ii,38*8+8+i*32,a.charAt(i),MIKA_romaji_scale); /* ローマ字表示を消去 */
			}
			for(i=0;i<a_length;i++)
			{
				cslputu(g,ii+4,38*8+i*32,"aaaa",1,MIKA_bk_color); /* 下線を消去 */
			}
		}
	}
	void prepflags(int flag) /* ランダム練習 英単語練習 ローマ字練習の開始時のフラグクリア処理 */
	{
		MIKA_c_p1=0; /* 練習文字 横座標 クリア */
		MIKA_c_p2=0; /* 練習文字 縦座標 クリア */
		MIKA_type_count=0; /* 入力文字数カウンター クリア */
		MIKA_type_err_count=0; /* エラー入力文字数カウンター クリア */
		MIKA_err_char_flag=0; /* エラー入力フラグ クリア */
		MIKA_type_speed=0.0; /* 文字入力速度 クリア */
		MIKA_type_speed2=0.0; /* ローマ字入力時の打鍵速度 クリア */
		MIKA_type_speed_time=0.0; /* 前回 練習経過時間 クリア */
		MIKA_ttype_speed_time=0.0; /* 今回 練習経過時間 クリア */
		MIKA_w_count=0; /* ひらがな入力文字数カウンター クリア */
		MIKA_r_count=0; /* ひらがな一文字内のローマ字表記文字カウンター クリア */
		MIKA_time_start_flag=0; /* 時間計測開始フラグ クリア */
		MIKA_utikiri_flag=0; /* 練習テキスト打ち切りフラグ クリア */
		MIKA_utikiri_flag2=0; /* 前回速度表示時の打ち切りフラグ クリア */
		MIKA_type_syuryou_flag=0; /* 練習終了時の記録更新フラグ クリア */

	}
	void dispupmes(Graphics g) /* タイプ速度を更新したときのメッセージを表示 */
	{
		cslcolor(g,MIKA_green); /* 表示色を緑色に設定 */
		cslput(g,20*16,20*8,MIKA_mesi3); /* 指定位置に「おめでとう、記録を更新しました」のメッセージを表示 */
	}
	void proctrainexit(Graphics g)/* ランダム練習 英単語練習の練習終了時の表示更新 */
	{
			dispkaisu2(g,1); /* 前回練習回数の表示を消去 */
			MIKA_p_count[MIKA_type_kind_no]++; /* 練習回数を加算 */
			dispkaisu2(g,0); /* 今回練習回数を表示 */
			dispabortmes2(g,1); /* エスケープキーを押すと中断しますのメッセージを消去 */
			dispretrymessage(g,0); /* リトライメッセージを表示 */
	}
	void proctrainexit2(Graphics g) /* ローマ字練習の練習終了時の表示更新 */
	{
			dispkaisu2(g,1); /* 前回練習回数の表示を消去 */
			MIKA_p_count[MIKA_type_kind_no]++; /* 練習回数を加算 */
			dispkaisu2(g,0); /* 今回練習回数を表示 */
			dispabortmes2(g,1); /* エスケープキーを押すと中断しますのメッセージを消去 */
			dispretrymessage(g,0); /* リトライメッセージを表示 */
	}
	void prockiroku(Graphics g) /* ランダム練習 英単語練習 ローマ字練習にてタイプ入力速度が前回までの最高速度を更新したかの比較を行う */
	{
		if(MIKA_type_speed_record[MIKA_type_kind_no]<MIKA_type_speed) /* 前回までの最高入力速度を更新した場合 */
		{
			if(MIKA_type_speed_record[MIKA_type_kind_no]>0.0) /* 前回の最高入力速度がゼロより大きい場合 */
			{
				dispupmes(g); /* 練習記録を更新しましたのメッセージを表示 */
				MIKA_type_syuryou_flag=2; /* 練習記録更新フラグを2にセット */
			}
			else /* 前回の最高入力速度がゼロの場合 */
			{
				MIKA_type_syuryou_flag=1; /* 練習記録更新フラグを1にセット */
			}
			MIKA_type_kiroku_date=new Date(); /* 最高記録達成日の日時を取得 */
			SimpleDateFormat MIKA_date=new SimpleDateFormat("yy/MM/dd"); /* 最高記録達成日時の取り出しフォーマットを作成 */
			MIKA_type_date=MIKA_date.format(MIKA_type_kiroku_date); /* 最高記録達成日時文字列を指定フォーマットに従って作成 */
//			System.out.printf("日付=%s\n",MIKA_type_date);
			MIKA_file_error_hayasa=whayasa(); /* 最高速度記録ファイル書き込み */
		}
	}
	void procdispspeed(Graphics g) /* ランダム練習 英単語練習 入力速度表示 */
	{
			disptime(g,1); /* 前回練習経過時間表示を消去 */
			dispspeedrate(g,1); /* 前回 入力速度表示を消去 */
			MIKA_type_speed_time=MIKA_ttype_speed_time; /* 練習経過時間を更新 */
 			MIKA_type_speed=ftypespeed(MIKA_type_count,MIKA_type_start_time,MIKA_type_end_time); /* 入力速度を計算 */
			disptime(g,0); /* 今回練習経過時間を表示 */
			dispspeedrate(g,0); /* 今回入力速度を表示 */
	}
	void procdispspeed2(Graphics g) /* ローマ字練習 入力速度表示 */
	{
			disptime(g,1); /* 前回練習経過時間表示を消去 */
			dispspeedrate2(g,1); /* 前回 ローマ字入力速度表示を消去 */
			MIKA_type_speed_time=MIKA_ttype_speed_time; /* 練習経過時間を更新 */
 			MIKA_type_speed=ftypespeed(MIKA_w_count,MIKA_type_start_time,MIKA_type_end_time); /* ひらがな文字入力速度を計算 */
	 		MIKA_type_speed2=ftypespeed(MIKA_type_count,MIKA_type_start_time,MIKA_type_end_time); /* 打鍵速度を計算 */ 
			disptime(g,0); /* 今回練習経過時間を表示 */
			dispspeedrate2(g,0); /* 今回ローマ字入力速度を表示 */
	}
	double roundtime(double time) /* 小数点以下 切り捨て */
	{
		long time0;
		time0=(long)time; /* 浮動小数点を整数に変換 */
		time=time0; /* 整数を浮動小数点に変換 */
		return time;
	}
	synchronized void proctrain(Graphics g,char nChar) /* ランダム練習 英単語練習の文字入力処理 */
	{
		if(nChar==0x1b){ /* エスケープキー入力の場合 */
			if(MIKA_practice_end_flag==0) /* 入力練習実行中の場合 */
			{
				MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
				if(MIKA_time_start_flag==1) /* 最初の正解を入力済で制限時間のタイマーを開始済の場合 */
				{
					MIKA_Procrtimer.cancel();	 /* 制限時間60秒のタイマーをキャンセル */				
					MIKA_type_end_time=System.currentTimeMillis(); /* 終了時間をミリ秒で取得 */
					MIKA_ttype_speed_time=(MIKA_type_end_time-MIKA_type_start_time)/1000.0; /* 練習時間 秒を計算 */
					if(MIKA_ttype_speed_time<=0.0)MIKA_ttype_speed_time=1.0; /* 練習時間がゼロ以下の場合は1に設定 */
					MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+(long)MIKA_ttype_speed_time; /* 累積練習時間の記録を加算 */
				}
				dispabortmes2(g,1); /* エスケープキーで終了しますの表示を消去 */
				dispretrymessage(g,0); /* 練習リトライメッセージ表示 */
			}
			else /* 練習終了の場合 */
			{
				if(MIKA_type_syuryou_flag==1||MIKA_type_syuryou_flag==2) /* 練習記録を更新した場合 */
				{
					MIKA_type_speed_record[MIKA_type_kind_no]=MIKA_type_speed;	/* 練習記録 最高入力速度を更新 */
					MIKA_type_date_record[MIKA_type_kind_no]=MIKA_type_date; /* 練習記録 達成日を更新 */
				}
				MIKA_exec_func_no=funcbackmenu(MIKA_exec_func_no);	/* メニューを一階層戻る */
				dispmen(g); /* メニュー表示 */
			}
		}
		else if((nChar==0x0d||nChar==0x0a)&&MIKA_practice_end_flag==1)	 /* 練習の終了時に改行が入力された場合 */
		{
			MIKA_practice_end_flag=0; /* 練習実行中フラグクリア */
			if(MIKA_type_syuryou_flag==1||MIKA_type_syuryou_flag==2)	 /* 練習記録を更新した場合 */
			{
					MIKA_type_speed_record[MIKA_type_kind_no]=MIKA_type_speed; /* 練習記録 最高入力速度を更新 */
					MIKA_type_date_record[MIKA_type_kind_no]=MIKA_type_date; /* 練習記録 達成日を更新 */
			}
			if(600<MIKA_exec_func_no&&MIKA_exec_func_no<700) intwordtable(MIKA_word_table,MIKA_type_speed_record[MIKA_type_kind_no]);  /* 英単語練習 練習テキスト作成 */
			else inctable(MIKA_char_table,MIKA_type_speed_record[MIKA_type_kind_no]); /* ランダム練習 練習テキスト作成 */
			prepflags(0); /* 練習フラグ初期化 */
			dispmen(g); /* 画面表示 */
		}
		else if(MIKA_practice_end_flag==0) /* 練習実行中の場合 */
		{
			if(MIKA_time_start_flag==1) /* 最初の正解を入力済の場合 */
			{
				MIKA_type_end_time=System.currentTimeMillis();  /*終了時間をミリ秒で取得 */
				MIKA_ttype_speed_time=(MIKA_type_end_time-MIKA_type_start_time)/1000.0; /* 練習時間 秒を計算 */
				if(MIKA_ttype_speed_time>=MIKA_random_key_limit)  /* 練習時間が制限時間を超えた場合 */
				{
					if(MIKA_practice_end_flag==0) /* タイマーによる割り込みを考慮して再度フラグをチェック */
					{
						MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
						MIKA_Procrtimer.cancel();		/* 制限時間60秒のタイマーをキャンセル */				
						MIKA_ttype_speed_time=MIKA_random_key_limit; /* 練習時間を制限時間に設定 */
						MIKA_type_end_time=MIKA_type_start_time+(long)(MIKA_random_key_limit*1000); /* 終了時間を開始時間+制限時間に設定 */
						procdispspeed(g); /* 入力速度を表示 */
						MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+(long)MIKA_ttype_speed_time; /* 累積練習時間の記録を加算 */
						prockiroku(g); /* 記録を更新時の処理 */
						proctrainexit(g); /* 練習終了時の表示更新 */
					}
					return;
				}
			}
			MIKA_key_char=MIKA_chat_t[MIKA_c_p2][MIKA_c_p1]; /* 練習文字を取り出し */
			if(uppertolower(nChar)==uppertolower(MIKA_key_char)) /* 入力文字と練習文字を小文字に変換して比較 */
			{
				 /* 練習文字と入力文字が一致する場合 */
				if(MIKA_type_count+1>=MIKA_cline_c) /* すべての練習文字を入力した場合は練習を終了 */
				{
					if(MIKA_practice_end_flag==0) /* タイマーによる割り込みを考慮して再度フラグをチェック */
					{
						MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
						MIKA_Procrtimer.cancel();	/* 	制限時間60秒のタイマーをキャンセル */				
						MIKA_type_count++; /* 入力文字正解数を加算 */
						MIKA_utikiri_flag=1; /* 練習打ち切りフラグをセット */
						MIKA_utikiri_flag2=0; /* 前回練習速度消去用にフラグをクリア */
						if(MIKA_err_char_flag==1) /* 前回入力がエラーの場合 */
						{
						MIKA_err_char_flag=0; /* エラー入力フラグクリア */
						disperrchar(g,0); /* エラー文字の赤色表示を元の背景色に戻す */
						}
						cslputu(g,MIKA_t_line*16+MIKA_c_p2*20,MIKA_c_p1*16,"aa",1,MIKA_color_text_under_line); /* 正解文字に下線を表示 */
						if(MIKA_c_p1<39) /* 次の練習文字位置を取得 */
						{
							MIKA_c_p1++; /* 横座標インクリメント */
						}
						else
						{
							MIKA_c_p1=0; /* 横座標をゼロに設定*/
							MIKA_c_p2++; /* 縦座標をインクリメント */
						}
				
						procdispspeed(g); /* 入力速度を表示 */
						MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+(long)MIKA_ttype_speed_time; /* 累積練習時間の記録を加算 */
						prockiroku(g); /* 記録を更新時の処理 */
						proctrainexit(g); /* 練習終了時の表示更新 */
					}
					return;
				}
				MIKA_type_count++; /* 入力文字正解数を加算 */
				if(MIKA_time_start_flag==0) /* 最初の正解文字入力の場合 */
				{
					MIKA_type_start_time=System.currentTimeMillis(); /* 練習開始時間をミリ秒で取得取得 */
					MIKA_type_speed_time=0; /* 前回練習時間秒を0に設定 */
					MIKA_ttype_speed_time=0; /* 今回練習時間秒を0に設定 */
					MIKA_time_start_flag=1; /* 練習時間計測フラグセット */
					MIKA_Procrtimer = new Procrtimer();/* 60秒タイマー取得 */
					MIKA_timer.scheduleAtFixedRate(MIKA_Procrtimer,MIKA_random_time_interval,MIKA_random_time_interval); /* タイマーを一秒間隔でセット */
				}
				if(MIKA_err_char_flag==1) /* 前回入力がエラーの場合 */
				{
					MIKA_err_char_flag=0; /* エラー入力フラグクリア */
					disperrchar(g,0); /* エラー文字の赤色表示を元の背景色に戻す */
				}
				cslputu(g,MIKA_t_line*16+MIKA_c_p2*20,MIKA_c_p1*16,"aa",1,MIKA_color_text_under_line); /* 正解文字に下線を表示 */
				if(MIKA_c_p1<39) /* 次の練習文字位置を取得 */
				{
					MIKA_c_p1++; /* 横座標インクリメント */
				}
				else
				{
					MIKA_c_p1=0; /* 横座標をゼロに設定*/
					MIKA_c_p2++; /* 縦座標をインクリメント */
				}
			}
			else /* 入力エラーの場合 */
			{
				MIKA_err_char_flag=1; /* エラー入力フラグセット */
				disperrchar(g,1); /* エラー文字を背景赤で表示 */
				disperror1(g,1); /* 前回のエラー回数表示をクリア */
				MIKA_type_err_count++; /* エラー入力文字数カウンターを加算 */
				disperror1(g,0); /* 今回エラー回数を表示 */
			}
//			if(MIKA_time_start_flag==1) /* 練習時間計測中の場合 */ /* 2023/6/5 削除 */
//			{
//				if((roundtime(MIKA_type_speed_time)!=roundtime(MIKA_ttype_speed_time))&&MIKA_ttype_speed_time>=1.0) /* 練習時間が前回より一秒以上更新している場合は入力速度を更新 */ /* 2023/6/5 削除 */
//				{
//					procdispspeed(g); /* 入力速度を表示 */ /* 2023/6/5 削除 */
//				}
//			}
		}
	}
	char uppertolower(char nChar) /* 英大文字を英小文字に変換 */
	{
			if('A'<=nChar&&nChar<='Z') nChar=(char)(nChar-'A'+'a'); /* 英大文字の場合は小文字に変換 */
			return nChar;
	}
	synchronized void procatrain(Graphics g,char nChar) /* ローマ字ランダム練習 ローマ字単語練習 文字入力処理 */
	{
		if(nChar==0x1b){ /* エスケープキー入力の場合 */
			if(MIKA_practice_end_flag==0) /* 入力練習実行中の場合 */
			{
				MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
				if(MIKA_time_start_flag==1) /* 最初の正解を入力済で制限時間のタイマーを開始済の場合 */
				{				
					MIKA_Procatimer.cancel();	/* 制限時間60秒のタイマーをキャンセル */				
					MIKA_type_end_time=System.currentTimeMillis(); /*終了時間をミリ秒で取得 */
					MIKA_ttype_speed_time=(MIKA_type_end_time-MIKA_type_start_time)/1000.0; /* 練習時間 秒を計算 */
					if(MIKA_ttype_speed_time<=0.0)MIKA_ttype_speed_time=1.0; /* 練習時間がゼロ以下の場合は1に設定 */
					MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+(long)MIKA_ttype_speed_time; /* 累積練習時間の記録を加算 */
				}
				dispabortmes2(g,1); /* エスケープキーで終了しますの表示を消去 */
				dispretrymessage(g,0); /* 練習リトライメッセージ表示 */
			}
			else /* 練習終了の場合 */
			{
				if(MIKA_type_syuryou_flag==1||MIKA_type_syuryou_flag==2) /* 練習記録を更新した場合 */
				{
					MIKA_type_speed_record[MIKA_type_kind_no]=MIKA_type_speed; /* 練習記録 最高入力速度を更新 */
					MIKA_type_date_record[MIKA_type_kind_no]=MIKA_type_date; /* 練習記録 達成日を更新 */
				}
				MIKA_exec_func_no=funcbackmenu(MIKA_exec_func_no); /* メニューを一階層戻る */
				dispmen(g); /* メニュー表示 */
			}
		}
		else if((nChar==0x0d||nChar==0x0a)&&MIKA_practice_end_flag==1) /* 練習の終了時に改行が入力された場合 */
		{
			MIKA_practice_end_flag=0; /* 練習実行中フラグクリア */
			if(MIKA_type_syuryou_flag==1||MIKA_type_syuryou_flag==2) /* 練習記録を更新した場合 */
			{
					MIKA_type_speed_record[MIKA_type_kind_no]=MIKA_type_speed; /* 練習記録 最高入力速度を更新 */
					MIKA_type_date_record[MIKA_type_kind_no]=MIKA_type_date; /* 練習記録 達成日を更新 */
			}
			if(MIKA_exec_func_no==701) 
			{
				inatable(MIKA_kana,MIKA_type_speed_record[MIKA_type_kind_no]); /* ローマ字ランダム練習 練習テキスト作成 */
			}
			else
			{
				intawordtable(MIKA_romaji_tango_table,MIKA_type_speed_record[MIKA_type_kind_no]); /* ローマ字単語練習 練習テキスト作成 */
			}
			prepflags(0); /* 練習フラグ初期化 */
			getromaji(MIKA_w_count); /* 最初の練習文字のローマ字読み取得 */
			dispmen(g); /* 画面表示 */
		}
		else if(MIKA_practice_end_flag==0) /* 練習実行中の場合 */
		{
			if(MIKA_time_start_flag==1) /* 最初の正解を入力済の場合 */
			{
				MIKA_type_end_time=System.currentTimeMillis();  /*終了時間をミリ秒で取得 */
				MIKA_ttype_speed_time=(MIKA_type_end_time-MIKA_type_start_time)/1000.0; /* 練習時間 秒を計算 */
				if(MIKA_ttype_speed_time>=MIKA_random_key_limit) /* 練習時間が制限時間を超えた場合 */
				{					
					if(MIKA_practice_end_flag==0) /* タイマーによる割り込みを考慮して再度フラグをチェック */
					{
						MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
						MIKA_Procatimer.cancel();			/* 制限時間60秒のタイマーをキャンセル */			
						MIKA_ttype_speed_time=MIKA_random_key_limit; /* 練習時間を制限時間に設定 */
						MIKA_type_end_time=MIKA_type_start_time+(long)(MIKA_random_key_limit*1000); /* 終了時間を開始時間+制限時間に設定 */
						procdispspeed2(g); /* ローマ字入力速度を表示 */
						MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+(long)MIKA_ttype_speed_time; /* 練習時間の記録を加算 */
						prockiroku(g); /* 記録を更新時の処理 */
						proctrainexit2(g); /* 練習終了時の表示更新 */
					}
					return;
				}
			}
			nChar=uppertolower(nChar); /* 入力文字を小文字に変換 */
//			System.out.printf("char %x pressed %x %x \n",(int) nChar,(int)MIKA_key_char,(int)MIKA_key_char2);
			if (MIKA_key_char==nChar||MIKA_key_char2==nChar) /* 入力文字が正解の場合 */
			{
				if(MIKA_key_char==' '||((MIKA_key_char==nChar)&&(MIKA_r_count+1>=MIKA_romaji_length))||((MIKA_key_char2==nChar)&&(MIKA_r_count+1>=MIKA_romaji_length2))) /* 正解がスペースかローマ字入力文字数がひらがなローマ字表記の文字数を超えた場合 */
				{
					if(MIKA_w_count+1>=MIKA_cline_c) /* すべての練習文字を入力した場合は練習を終了 */
					{
						if(MIKA_practice_end_flag==0) /* タイマーによる割り込みを考慮して再度フラグをチェック */
						{
							MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
							MIKA_Procatimer.cancel();	/* 	制限時間60秒のタイマーをキャンセル */			
							MIKA_w_count++; /* 正解数を加算 */
							MIKA_type_count++; /* 入力打鍵数加算 */
							if (MIKA_err_char_flag==1) /* 前回入力がエラーの場合 */
							{
								MIKA_err_char_flag=0; /* エラー入力フラグクリア */
								disperrchar(g,0); /* ひらがなの練習文字を背景白色で再表示 */
								dispromaji(g,MIKA_romaji,0); /* ひらがなのローマ字ガイドを背景白色で再表示 */
							}
							cslputu(g,2*16+6,38*8+MIKA_r_count*32,"aaaa",1,MIKA_color_romaji_under_line); /* ローマ字ガイドの正解に下線を表示 */
							MIKA_r_count++;
							cslputu(g,MIKA_t_line*16+MIKA_c_p2*20,(MIKA_c_p1)*16,"aa",1,MIKA_color_text_under_line); /* ひらがなの練習文字に下線を表示 */
							MIKA_utikiri_flag=1; /* 練習打ち切りフラグをセット */
							MIKA_utikiri_flag2=0; /* 前回練習速度消去用にフラグをクリア */
							procdispspeed2(g); /* ローマ字入力速度を表示 */
							MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+(long)MIKA_ttype_speed_time; /* 累積練習時間の記録を加算 */
							prockiroku(g); /* 記録を更新時の処理 */
							proctrainexit2(g); /* 練習終了時の表示更新 */
							if(MIKA_c_p1<39) /* 次回の練習文字位置を算出 */
							{
								MIKA_c_p1++; /* 横座標インクリメント */
							}
							else
							{
								MIKA_c_p1=0; /* 横座標をゼロに設定*/
								MIKA_c_p2++; /* 縦座標をインクリメント */
							}
						}
						return;
					}
				}
				MIKA_type_count++; /* 入力打鍵数加算 */
				if(MIKA_time_start_flag==0) /* 最初の正解文字入力の場合 */
				{
					MIKA_type_start_time=System.currentTimeMillis(); /* 練習開始時間をミリ秒で取得取得 */ 
					MIKA_type_speed_time=0; /* 前回練習時間秒を0に設定 */
					MIKA_ttype_speed_time=0; /* 今回練習時間秒を0に設定 */
					MIKA_time_start_flag=1; /* 練習時間計測フラグセット */
					MIKA_Procatimer = new Procatimer(); /* ローマ字練習用60秒タイマー取得 */
					MIKA_timer.scheduleAtFixedRate(MIKA_Procatimer,MIKA_random_time_interval,MIKA_random_time_interval); /* タイマーを一秒間隔でセット */
			}
				if(MIKA_key_char!=nChar&&MIKA_key_char2==nChar) /* 正解が二番目表記のローマ字の場合 */
				{
					dispromaji(g,MIKA_romaji,1); /* 表示中のローマ字を消去 */
					MIKA_key_char=MIKA_key_char2; /* 二番目の表記のローマ字の文字を一番目の表記の文字に設定 */
					MIKA_key_char2=0; /* 二番目の表記のローマ字の文字をクリア */
					MIKA_romaji=MIKA_romaji2; /* 二番目の表記のローマ字を一番目の表記のローマ字に設定 */
					MIKA_romaji_length=MIKA_romaji_length2; /* 二番目の表記のローマ字の文字数を一番目の表記の文字数に設	*/
					MIKA_romaji2=null; /* 二番目の表記のローマ字をクリア */
					MIKA_romaji_length2=0; /* 二番目の表記のローマ字の文字数をクリア */
					dispromaji(g,MIKA_romaji,0); /* ローマ字再表示 */
				}
				else if(MIKA_key_char==nChar&&MIKA_key_char2!=nChar) /* 正解が一番目の表記のローマ字の場合 */
				{
					MIKA_key_char2=0;  /* 二番目の表記のローマ字の文字をクリア */
					MIKA_romaji2=null; /* 二番目の表記のローマ字をクリア */
					MIKA_romaji_length2=0; /* 二番目の表記のローマ字の文字数をクリア */
				}
				if (MIKA_err_char_flag==1) /* 前回入力がエラーの場合 */
				{
					MIKA_err_char_flag=0; /* エラー入力フラグクリア */
					disperrchar(g,0); /* ひらがなの練習文字を背景白色で再表示 */
					dispromaji(g,MIKA_romaji,0); /* ひらがなのローマ字ガイドを背景白色で再表示 */
				}
				cslputu(g,2*16+6,38*8+MIKA_r_count*32,"aaaa",1,MIKA_color_romaji_under_line); /* ローマ字ガイドの正解に下線を表示 */
				MIKA_r_count++; /* 入力ローマ字位置加算 */
				if(MIKA_key_char==' '||MIKA_r_count>=MIKA_romaji_length) /* 正解がスペースかローマ字入力文字数がひらがなローマ字表記の文字数を超えた場合 */
				{
					MIKA_w_count++; /* 正解数を加算 */
					cslputu(g,MIKA_t_line*16+MIKA_c_p2*20,(MIKA_c_p1)*16,"aa",1,MIKA_color_text_under_line); /* ひらがなの練習文字に下線を表示 */
					if(MIKA_c_p1<39) /* 次回の練習文字位置を算出 */
					{
						MIKA_c_p1++; /* 横座標インクリメント */
					}
					else
					{
						MIKA_c_p1=0; /* 横座標をゼロに設定*/
						MIKA_c_p2++; /* 縦座標をインクリメント */
					}
					MIKA_r_count=0; /* ひらがなのローマ字表記の文字カウンターをゼロに設定 */
					dispromaji(g,MIKA_romaji,1); /* 前回表示のローマ字を表示消去 */
					getromaji(MIKA_w_count); /* 次回のローマ字の読みを取得 */
					dispromaji(g,MIKA_romaji,0); /* 次回のローマ字の読みを表示 */
				}
				else
				{
					MIKA_key_char=MIKA_romaji.charAt(MIKA_r_count); /* ローマ字表記の次の文字を取得 */
					if(MIKA_romaji2!=null) /* 二番目の読みのローマ字がある場合 */
					{
						MIKA_key_char2=MIKA_romaji2.charAt(MIKA_r_count); /* 二番目のローマ字の次の文字を取得 */
					}
					else MIKA_key_char2=0; /* 二番目のローマ字の読みが無い場合はゼロを設定 */
				}
			}
			else  /* 入力エラーの場合 */
			{
				MIKA_err_char_flag=1; /* エラー入力フラグセット */
				disperror2(g,1); /* 前回エラー回数表示をクリア */
				MIKA_type_err_count++; /* エラー回数カウンターを加算 */
				disperror2(g,0); /* 今回エラー回数表示 */
				disperrchar(g,1); /* エラー文字のひらがなを背景赤で表示 */
				dispromaji(g,MIKA_romaji,0); /*   間違えたローマ字表記文字を背景赤で表示 */
			}
//			if(MIKA_time_start_flag==1) /* 練習時間計測中の場合 */ /* 2023/6/5 削除 */
//			{
//				if((roundtime(MIKA_type_speed_time)!=roundtime(MIKA_ttype_speed_time))&&MIKA_ttype_speed_time>=1.0) /* 練習時間が前回より一秒以上更新している場合は入力速度を更新 */ /* 2023/6/5 削除 */
//				{
//					procdispspeed2(g); /* ローマ字入力速度を表示 */ /* 2023/6/5 削除 */
//				}
//			}
		}
	}
	void getromaji(int w_count) /* ひらがなのローマ字表記を取得 */
	{
		int c_point;
		int romaji_length;
		c_point=MIKA_chat_yomi_t[w_count]; /* ローマ字テーブル位置を取得 */
		if(c_point!=0)  /* ローマ字テーブル位置がゼロでない場合 */
		{
			MIKA_romaji=MIKA_kana_yomi[c_point-1]; /* ひらがなの一番目のローマ字表記を取得 */
			if(MIKA_romaji!=null)
			{
				MIKA_key_char=MIKA_romaji.charAt(0); /* 一番目のローマ字表記の最初のアルファベット一文字を取得 */
				MIKA_romaji_length=MIKA_romaji.length();
			}
			else
			{
				MIKA_key_char=0; /* 文字にゼロを設定 */
				MIKA_romaji_length=0;
			}
			MIKA_romaji2=MIKA_kana_yomi2[c_point-1]; /* ひらがなの二番目のローマ字表記を取得 */
			if(MIKA_romaji2!=null)
			{
				MIKA_key_char2=MIKA_romaji2.charAt(0);/* 二番目のローマ字表記の最初のアルファベット一文字を取得 */
				MIKA_romaji_length2=MIKA_romaji2.length();
			}
			else
			{
				MIKA_key_char2=0; /* 文字にゼロを設定 */
				MIKA_romaji_length2=0;
			}
		}
		else /* ローマ字テーブル位置がゼロの場合(文字がスペースの場合)*/
		{
			MIKA_romaji=null;
			MIKA_romaji2=null;
			MIKA_romaji_length=0;
			MIKA_romaji_length2=0;
			MIKA_key_char=' '; /* 一番目のローマ字表記のアルファベットにスペース文字を設定 */
			MIKA_key_char2=0; /* 二番目のローマ字表記のアルファベットにゼロを設定 */
		}
	}
	void inctable(String a,double speed) /* ランダム練習 練習テキスト作成 */
	{
		incctable(a,null,speed,0); /* ランダム練習 ローマ字ランダム練習 共通処理呼び出し */
	}
	void inatable(char[] b,double speed) /* ローマ字練習 練習テキスト作成 */
	{
		incctable(null,b,speed,1); /* ランダム練習 ローマ字ランダム練習 共通処理呼び出し */
	}
	void incctable(String a,char[] b,double speed,int flag) /* ランダム練習 ローマ字ランダム練習 練習テキスト作成 */
// a 文字列テーブル ランダム練習用
// b 文字列テーブル ローマ字ランダム練習用
// speed 最高入力速度
// flag=0 ランダム練習 flag=1 ローマ字ランダム練習 */
	{
		int size_yoko=40; 
		double rsize_yoko=40.0;
		int i,j,k,kk,a_length,ii;
		Random rand=new Random(); /* 乱数処理作成 */
		MIKA_cline_x=(int)Math.ceil((speed+rsize_yoko)/rsize_yoko); /* 最大練習行数算出 */
		if(MIKA_cline_x>10) MIKA_cline_x=10; /*最大練習行数は 10行 */
		if(MIKA_cline_x<3) MIKA_cline_x=3; /* 最小練習行数は 3行 */
		MIKA_cline_c=MIKA_cline_x*size_yoko; /* 最大文字数算出 */
		if(flag==0) /* ランダム練習の場合 */
		{
			a_length=a.length();  /* 文字列長取得 */
		}
		else /* ローマ字ランダム練習の場合 */
		{
			a_length=b.length; /* 文字列長取得 */
		}
		k=0;
		kk=0;
		for(j=0;j<MIKA_cline_x;j++) /* 最大行まで文字を設定 */
		{
			for(i=0;i<size_yoko;i++) /* 一行分の文字設定 */
			{
				if(kk==5) /* 五文字目の場合 */
				{
					kk=0; /* 五文字をカウントするカウンターをゼロ設定 */
					MIKA_chat_t[j][i]=' '; /* スペース文字設定 */
					if(flag!=0) /* ローマ字ランダム練習の場合 */
					{
						MIKA_chat_yomi_t[k]=0; /* 読みテーブルにゼロを設定 */
					}
					k++; /* 練習文字数を 一加算 */
				}
				else
				{
					ii=rand.nextInt(a_length); /* ランダムに文字列長以下の整数を取得 */
					if(flag==0) /* ランダム練習の場合 */
					{
						MIKA_chat_t[j][i]=a.charAt(ii); /* 練習文字をランダムに設定 */
					}
					else /* ローマ字ランダム練習の場合 */
					{
						MIKA_chat_t[j][i]=b[ii]; /* 練習文字をランダムに設定 */
						MIKA_chat_yomi_t[k]=ii+1; /* 読みテーブルをひらがな文字番号を設定 */
					}
					k++; /* 練習文字数を一加算 */
					kk++; /* 五文字をカウントするカウンターを一加算 */
				}
			}
		}
		if(MIKA_chat_t[MIKA_cline_x-1][39]==' ') MIKA_cline_c--; /* 最終文字がスペースの時は練習文字数を一減算 */
	}
	int kfound(char a) /* かな文字のかな文字テーブル内の位置を取得 */
	{
		int i;
		int length;
		length=MIKA_kana.length; /*かな文字テーブルの長さを取得 */
		for(i=0;i<length;i++) /* かな文字テーブル内をサーチ */
		{
			if(a==MIKA_kana[i]) /* かな文字とかな文字テーブルの文字が一致する場合 */
			return i+1; /* 一致する番号を一加算してリターン */
		}
		return 0; /* 一致するかな文字がない場合はゼロでリターン */
	}
	void intwordtable(String[] a,double speed) /* 英単語練習の練習テキスト作成 */
	{
			inttangotable(a,speed,0); /* 英単語練習 ローマ字単語練習のテキスト作成共通処理呼び出し */
	}
	void intawordtable(String[] a,double speed) /* ローマ字単語練習の練習テキスト作成 */
	{
			inttangotable(a,speed,1); /* 英単語練習 ローマ字単語練習のテキスト作成共通処理呼び出し */
	}
	void inttangotable(String[] a,double speed,int flag) /* 英単語練習 ローマ字単語練習の練習テキスト作成共通処理 */
/* a 単語テーブル speed 最高入力速度 flag =0 英単語練習 =1 ローマ字単語練習 */
	{
		int size_yoko=40; /* 横方向文字数 整数 */
		String b;
		char c;
		int space_flag=0;
		double rsize_yoko=40.0; /* 横方向の文字数 実数 */
		int i,ii,j,k,kk,l,a_length,b_length;
		Random rand=new Random(); /* 乱数処理作成 */
		MIKA_cline_x=(int)Math.ceil((speed+rsize_yoko)/rsize_yoko); /* 最大練習行数算出 */
		if(MIKA_cline_x>10) MIKA_cline_x=10; /*最大練習行数は 10行 */
		if(MIKA_cline_x<3) MIKA_cline_x=3; /* 最小練習行数は 3行 */
		MIKA_cline_c=MIKA_cline_x*size_yoko; /* 最大文字数算出 */
		a_length=a.length; /* 単語テーブルの単語数を取得 */
		kk=0; /* 練習文字数カウンター ゼロ設定 */
		i=0; /* 横方向の文字位置カウンターをゼロ設定 */
		j=0; /* 練習行数をゼロ設定 */
		for(l=0;l<1000;l++)
		{
			ii=rand.nextInt(a_length); /* ランダムに単語テーブル長以下の整数を取得 */
			b=a[ii]; /* 単語アドレス取得 */
			b_length=b.length(); /* 単語長さを取得 */
			if(kk+b_length>MIKA_cline_c) break; /* 残りエリアが単語長より短い場合は処理を中断 */
			space_flag=0; /* スペース設定フラグをゼロクリア */
			for(k=0;k<b_length;k++) /* 単語文字数分の練習文字を設定 */
			{
				c=b.charAt(k); /* 単語のk番目の文字を取得 */
				MIKA_chat_t[j][i]=c; /* 練習文字を設定 */
				if(flag==1) /* ローマ字単語設定の場合 */
				{
					MIKA_chat_yomi_t[kk]=kfound(c); /* ひらがな文字の読みを設定 */
				}
				kk++; /* 練習文字数を一加算 */
				if(kk>=MIKA_cline_c) break; /* 残りエリアがない時は処理を中断 */
				i++;
				if(i>=size_yoko) /* 行の終わりまで設定した場合 */
				{
					/* 練習文字アドレスを更新 */
					i=0; /* 横方向文字位置を行の先頭に設定 */
					j++; /* 練習行数を一加算 */
				} 
			}
			if(kk>=MIKA_cline_c) break; /* 残りエリアがない時は処理を中断 */
			MIKA_chat_t[j][i]=' '; /* スペース文字を設定 */
			if(flag==1) /* ローマ字単語設定の場合 */
			{
				MIKA_chat_yomi_t[kk]=0; /* スペース文字の読みをゼロに設定 */
			}
			space_flag=1; /* スペース設定フラグを一に設定 */
			kk++; /* 練習文字数を一加算 */
			if(kk>=MIKA_cline_c) break; /* 残りエリアがない時は処理を中断 */
			i++; /* 一行内の横方向の文字位置を一加算 */
			if(i>=size_yoko) /* 行の終わりまで設定した場合 */
			{    			
				/* 練習文字アドレスを更新 */
				i=0; /* 横方向文字位置を行の先頭に設定 */
				j++; /* 練習行数を一加算 */
			}
		}
		MIKA_cline_c=kk; /* 練習文字数を設定 */
		if(space_flag==1)  MIKA_cline_c--; /* 最終設定文字がスペースの時は練習文字数を一減算 */
//		System.out.printf("練習文字数 =%d\n",MIKA_cline_c);
	}
	Dimension keycord(char a) /* 練習文字に対応した キーの位置 列と行を取得 */
	{
		int x_pos;
		int y_pos;
		Dimension pos;
		if(0x41<=a&&a<=0x5a) /* アルファベット大文字の場合 */
		{
			x_pos=MIKA_pasciix[a-0x41]; /* アルファベット位置テーブルよりx位置取得 */
			y_pos=MIKA_pasciiy[a-0x41]; /* アルファベット位置テーブルよりy位置取得 */
		}
// ポジション練習で刻印の文字が大文字の時にガイドキー表示とエラーキー表示が正常に作動しないバグを修正 */
//		if(0x61<=a&&a<=0x7a) /* アルファベット小文字の場合 */ /* 2023/2/24修正 旧コード */
		else if(0x61<=a&&a<=0x7a) /* アルファベット小文字の場合 */ /* 2023/2/24修正 新コード */
		{
			x_pos=MIKA_pasciix[a-0x61]; /* アルファベット位置テーブルよりx位置取得 */
			y_pos=MIKA_pasciiy[a-0x61];/* アルファベット位置テーブルよりy位置取得 */
		}
	else if(0x30<=a&&a<=0x39) /* 文字が数字の場合 */
		{
			x_pos=MIKA_pnumberx[a-0x30];/* 数字位置テーブルよりx位置取得 */
			y_pos=MIKA_pnumbery[a-0x30];/* 数字位置テーブルよりy位置取得 */
		}
		else
		{
			x_pos=0; /* x位置にゼロを設定 */
			y_pos=0; /* y位置にゼロを設定 */
		}
		pos=new Dimension(y_pos,x_pos);
		return pos;
	}
	int	randomchar(String char_table0,int char_position0) /* 前回と重複せずにランダムに文字位置を取得 */
// charposition =-1 初回の取得の場合
// charposition >=0 前回のランダム文字位置
	{
		int char_length,ii;
		if(char_table0==null) return(0);
		Random rand=new Random(); /* 乱数処理作成 */
		char_length=char_table0.length(); /* 文字テーブルの長さ取得 */
		if(char_length==0) return(0);
		if(char_position0==-1) /* 初回の乱数取得の場合 */
		{
			ii=rand.nextInt(char_length); /* 文字テーブルの長さを元に乱数を取得 */
			return(ii);
		}
		else /* 前回乱数取得の場合 */
		{
			ii=rand.nextInt(char_length-1); /* 文字テーブルの長さ－１を元に乱数を取得 */
			ii=ii+char_position0+1; /* 取得した乱数に前回の文字位置＋１を加算 */
			if(ii>=char_length) ii=ii-char_length; /* 文字位置が文字テーブル長を超えた場合の補正 */
			return(ii);	
		}
	}
	public synchronized void paint(Graphics g) {
		// 画面を塗りつぶす
		try { /* 2023/6/5 追加 */
			MIKA_semaphore.acquire(); /* セマフォー要求 */ /* 2023/6/5 追加 */
			MIKA_win_size=getSize(); /* 表示画面サイズ取得 */
//		System.out.printf("win size width=%d height=%d\n",MIKA_win_size.width,MIKA_win_size.height);
			MIKA_insets=getInsets(); /* 表示画面外枠サイズ取得 */
//		System.out.printf("Inset left=%d right=%d top=%d bottom=%d \n",MIKA_insets.left,MIKA_insets.right,MIKA_insets.top,MIKA_insets.bottom);
			dispmen(g); /* 画面表示 */
			MIKA_semaphore.release(); /* セマフォー解放 */ /* 2023/6/5 追加 */
		} catch(InterruptedException e) /* 2023/6/5 追加 */
		{	
			e.printStackTrace(); /* 2023/6/5 追加 */
		}
	}
	private class MyKeyAdapter extends KeyAdapter {

	    Graphics g;
		@Override
		public void keyPressed(KeyEvent e) {
			int err;
			int keyCode;
			char keyChar;
       		g = getGraphics(); /* Graphics 取得 */
			keyChar=e.getKeyChar(); /* 入力文字取得 */
//			keyCode=(int) keyChar;
//			System.out.printf("KeyChar=%4x\n",KeyCode);			
			if(keyChar!=KeyEvent.CHAR_UNDEFINED) /* 入力されたキーが有効な文字の場合 */
			{
				err=exec_func(g,keyChar); /* 入力文字に対応した処理を実行 */
			}
    	    g.dispose(); /* Graphics 破棄 */
		}

	}
	public class Procptimer extends TimerTask /* ポジション練習用タイマー */
	{
	    Graphics g;
		public synchronized void run(){
       		g = getGraphics();  /* Graphics 取得 */
			if(MIKA_practice_end_flag==0) /* 練習実行中の場合 */
			{
				MIKA_guide_char=MIKA_key_char; /* ガイドキー文字に練習文字を設定 */
				dikposit(g,MIKA_guide_char,0); /* ガイドキー文字のキー位置を表示 */
				difposit(g,MIKA_guide_char,0); /* ガイドキー文字の指位置を表示 */
			}
//				System.out.printf("Timer task\n");
			cancel(); /* タイマーをキャンセル */
	    	g.dispose(); /* Graphics 破棄 */
		}
	}
	public class Procrtimer extends TimerTask /* ランダム練習 単語練習用タイマー */
	{
	    Graphics g;
		int sec_count=0;
		public synchronized void run(){
			sec_count++;
			if(MIKA_practice_end_flag==0) /* 練習実行中の場合 */
			{
       			g = getGraphics();  /* Graphics 取得 */
				try { /* 2023/6/5 追加 */
						MIKA_semaphore.acquire(); /* セマフォー要求 */ /* 2023/6/5 追加 */
						if((MIKA_practice_end_flag==0)&&(sec_count>=MIKA_random_key_limit2)) /* 制限時間を超過した場合 */
						{
							MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
							cancel(); /* タイマーをキャンセル */
							MIKA_ttype_speed_time=MIKA_random_key_limit2; /* 経過時間を制限時間に設定 */
							MIKA_type_end_time=MIKA_type_start_time+(long)(MIKA_random_key_limit2*1000); /* 現在時刻を開始時間+制限時間に設定 */
							procdispspeed(g); /* 練習速度表示 */
							MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+(long)MIKA_ttype_speed_time; /* 累積練習時間加算 */
							prockiroku(g); /* 記録を更新時の処理 */
							proctrainexit(g); /* 練習終了時の表示更新 */
						}
						else if(MIKA_practice_end_flag==0)
						{
							MIKA_type_end_time=System.currentTimeMillis(); /* 現在時刻をミリ秒で取得 */
							MIKA_ttype_speed_time=(MIKA_type_end_time-MIKA_type_start_time)/1000.0; /* 経過秒を実数で計算 */
							if((MIKA_type_speed_time!=MIKA_ttype_speed_time)&&MIKA_ttype_speed_time>=1.0) 
							{
								procdispspeed(g); /* 入力速度を表示 */
							}
						}
						MIKA_semaphore.release(); /* セマフォー解放 */ /* 2023/6/5 追加 */
//				System.out.printf("Timer task\n");
				} catch(InterruptedException e) /* 2023/6/5 追加 */
				{	
					e.printStackTrace(); /* 2023/6/5 追加 */
				}
				g.dispose(); /* Graphics 破棄 */
			}
		}
	}
	public class Procatimer extends TimerTask /* ローマ字ランダム練習 ローマ字単語練習用タイマー */
	{
	    Graphics g;
		int sec_count=0;
		public synchronized void run(){
		sec_count++;
		if(MIKA_practice_end_flag==0) /* 練習実行中の場合 */
			{
	     		g = getGraphics(); /* Graphics 取得 */
				try { /* 2023/6/5 追加 */
						MIKA_semaphore.acquire(); /* セマフォー要求 */ /* 2023/6/5 追加 */
						if((MIKA_practice_end_flag==0)&&(sec_count>=MIKA_random_key_limit2)) /* 制限時間を超過した場合 */
						{
							MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
							cancel(); /* タイマーをキャンセル */
							MIKA_ttype_speed_time=MIKA_random_key_limit2;/* 経過時間を制限時間に設定 */
							MIKA_type_end_time=MIKA_type_start_time+(long)(MIKA_random_key_limit2*1000); /* 現在時刻を開始時間+制限時間に設定 */
//					System.out.printf("start time=%d end time =%d\n",MIKA_type_start_time,MIKA_type_end_time);
							procdispspeed2(g); /* ローマ字入力速度を表示 */
							MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+(long)MIKA_ttype_speed_time; /* 累積練習時間加算 */
							prockiroku(g); /* 記録を更新時の処理 */
							proctrainexit2(g); /* 練習終了時の表示更新 */
						}
						else if(MIKA_practice_end_flag==0)
						{
							MIKA_type_end_time=System.currentTimeMillis(); /* 現在時刻をミリ秒で取得 */
							MIKA_ttype_speed_time=(MIKA_type_end_time-MIKA_type_start_time)/1000.0; /* 経過秒を実数で計算 */
							if((MIKA_type_speed_time!=MIKA_ttype_speed_time)&&MIKA_ttype_speed_time>=1.0)
							{
								procdispspeed2(g); /* ローマ字入力速度を表示 */
							}
						}
						MIKA_semaphore.release(); /* セマフォー解放 */
					}
//				System.out.printf("Timer task\n");
				catch(InterruptedException e)
				{	 /* 2023/6/5 追加 */
					e.printStackTrace(); /* 2023/6/5 追加 */
				}
				g.dispose(); /* Graphics 破棄 */
			}
		}
	}
}
