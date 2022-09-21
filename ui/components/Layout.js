import Head from 'next/head'

export default function Layout({children, title=""}) {
    return (
            <div>
                <Head>
                    <div>
                        <link rel="icon" href="/favicon.ico"/>
                        <meta
                            name="description"
                            content="A website for RPG/PnP music playing"
                        />
                        <meta name="og:title" content={title}/>
                        <meta name="twitter:card" content="summary_large_image"/>
                    </div>
                    <title>{title}</title>
                </Head>
                <div>
                    <div className="layout">
                        <div className="main container-xl bg-dark text-light mt-10">
                            <div className="m-2 pt-2">
                                <div className="row">
                                    <div className="col-xl-12">
                                        <main className="mb-auto">
                                            {children}
                                        </main>
                                    </div>
                                </div>

                                <footer className="row mt-5 mb-5">
                                    <hr/>
                                    <div className="col-md-12">
                                        <div className="w-full mx-auto">
                                            <p className="text-center">
                                                &copy; PnPMusicApp
                                            </p>
                                        </div>
                                    </div>
                                </footer>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    )
}